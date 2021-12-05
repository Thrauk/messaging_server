package messaging_server.client.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.client.Client;
import messaging_server.client.models.Partner;
import messaging_server.client.utility.ClientToClientMessageSender;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.Consumer;
import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleMessage;
import messaging_server.rabbitMQ.MessageEvents;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ServerMessagesListener extends Consumer {
    public ServerMessagesListener(String queueName) {
        super(queueName);
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void listener() {
        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                SimpleEventMessage jsonMessage = objectMapper.readValue(message.getBody(), SimpleEventMessage.class);

                if (!ClientData.isConnected.get()) {
                    if (jsonMessage.getEventType().equals(MessageEvents.connectionRequestResponseSuccessful)) {
                        ClientData.isConnected.compareAndSet(false, true);
                        System.out.println("Client connected successfully");
                    } else {
                        System.out.println("Client connection failed");
                    }
                } else {
                    if (jsonMessage.getEventType().equals(MessageEvents.checkIfConnectedResponseSuccessful)) {
                        String receivedMessage = jsonMessage.getMessage();
                        String partnerUid = receivedMessage.split("-")[1];
                        System.out.println("Client " + partnerUid + " is connected");
                        ClientData.connectedPartners.add(new Partner(partnerUid, receivedMessage));

                        // to be moved in a function
                        System.out.print("Write message to send:");
                        String messageToSend = Client.reader.readLine();
                        ClientToClientMessageSender.sendMessage(receivedMessage, partnerUid, messageToSend);

                    } else if (jsonMessage.getEventType().equals(MessageEvents.checkIfConnectedResponseFailed)) {
                        System.out.println("Client is not connected, try again");
                        //Client.showMenu();

                    } else if (jsonMessage.getEventType().equals(MessageEvents.listenForNewMessages)) {
                        String client = jsonMessage.getMessage().split("-")[0];



                        PartnersMessagesConsumer consumer = new PartnersMessagesConsumer(jsonMessage.getMessage(), client);
                        consumer.thread.start();
                        ClientData.partnersMessagesConsumers.add(consumer);
                        System.out.println("Started listening for " + client + "'s messages");
                    } else if(jsonMessage.getEventType().equals(MessageEvents.disconnectedPartner)) {
                        partnerDisconnected(jsonMessage.getMessage());
                    }
                }
            }, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();
        }
    }

    private void partnerDisconnected(String partnerId) {
        Optional<Partner> connectedPartner = ClientData.connectedPartners
                .exportAsList()
                .stream()
                .filter(partner -> partner.getPartnerId().equals(partnerId))
                .findFirst();

        connectedPartner.ifPresent(ClientData.connectedPartners::removeElement);

        Optional<PartnersMessagesConsumer> connectedPartnerConsumer = ClientData.partnersMessagesConsumers
                .exportAsList()
                .stream()
                .filter(partnersMessagesConsumer -> partnersMessagesConsumer.getPartnerId().equals(partnerId))
                .findFirst();

        connectedPartnerConsumer.ifPresent(partnersMessagesConsumer -> {
            System.out.println("Partner disconnected " + partnerId);
            partnersMessagesConsumer.closeListener();
            partnersMessagesConsumer.thread.interrupt();
            ClientData.partnersMessagesConsumers.removeElement(partnersMessagesConsumer);
        });


    }
}
