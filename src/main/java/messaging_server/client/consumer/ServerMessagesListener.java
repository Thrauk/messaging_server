package messaging_server.client.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.client.Client;
import messaging_server.client.config.DefaultConfig;
import messaging_server.client.models.Partner;
import messaging_server.client.utility.ClientToClientMessageSender;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.Consumer;
import messaging_server.client.data.ClientData;
import messaging_server.rabbitMQ.MessageEvents;

import java.io.IOException;
import java.util.Optional;

public class ServerMessagesListener extends Consumer {
    public ServerMessagesListener(String queueName) {
        super(queueName);
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void listener() {
        try {
            this.consumerTag = channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                SimpleEventMessage jsonMessage = objectMapper.readValue(message.getBody(), SimpleEventMessage.class);

                if (!ClientData.isConnected.get()) {

                    if (jsonMessage.getEventType().equals(MessageEvents.connectionRequestResponseSuccessful)) {
                        ClientData.isConnected.compareAndSet(false, true);
                        System.out.println("Client connected successfully");
                    } else {
                        System.out.println("Client connection failed");
                        ClientData.connectionDupName.compareAndSet(false,true);
                    }
                } else {
                    if (jsonMessage.getEventType().equals(MessageEvents.checkIfConnectedResponseSuccessful)) {
                        String receivedMessage = jsonMessage.getMessage();
                        String partnerUid = receivedMessage.split("-")[1];
                        System.out.println("Client " + partnerUid + " is connected");
                        ClientData.addOrSetPartnerQueue(partnerUid, receivedMessage);

                        sendMessage(receivedMessage, partnerUid);
                        ClientData.waitForResponseBool.compareAndSet(false, true);

                    } else if (jsonMessage.getEventType().equals(MessageEvents.checkIfConnectedResponseFailed)) {
                        System.out.println("Client is not connected, try again");
                        ClientData.waitForResponseBool.compareAndSet(false, true);

                    } else if (jsonMessage.getEventType().equals(MessageEvents.listenForNewMessages)) {
                        String client = jsonMessage.getMessage().split("-")[0];

                        PartnersMessagesConsumer consumer = new PartnersMessagesConsumer(jsonMessage.getMessage(), client);
                        consumer.thread.start();
                        ClientData.addOrSetPartnerListener(client, consumer);
                    } else if (jsonMessage.getEventType().equals(MessageEvents.disconnectedPartner)) {
                        partnerDisconnected(jsonMessage.getMessage());
                    } else if (jsonMessage.getEventType().equals(MessageEvents.receiveConnectedClientsList)) {
                        System.out.println(jsonMessage.getMessage());
                        ClientData.gotResponse.compareAndSet(false, true);
                    } else if (jsonMessage.getEventType().equals(MessageEvents.changeClientMaxQueue)) {
                        DefaultConfig.nrMaxOfMessagesAccepted = Integer.parseInt(jsonMessage.getMessage());
                    } else if (jsonMessage.getEventType().equals(MessageEvents.changeClientTopicTTL)) {
                        DefaultConfig.topicMessageTTL = Integer.parseInt(jsonMessage.getMessage());
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
        //System.out.println("Partner disconnected " + partnerId);
        Optional<Partner> connectedPartner = ClientData.connectedPartners
                .exportAsList()
                .stream()
                .filter(partner -> partner.getPartnerId().equals(partnerId))
                .findFirst();

        connectedPartner.ifPresent(partner -> {
            partner.getPartnersMessagesConsumer().closeListener();
            ClientData.connectedPartners.removeElement(partner);
        });
    }

    public static void sendMessage(String queue, String partner) {
        System.out.println("Write messages. Enter 'EXIT' to return to main menu.");

        String message;
        try {
            System.out.print(ClientData.clientId + ": ");
            message = Client.reader.readLine();

            while (!message.equals("EXIT")) {
                ClientToClientMessageSender.sendMessage(queue, partner, message);
                System.out.print(ClientData.clientId + ": ");
                message = Client.reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
