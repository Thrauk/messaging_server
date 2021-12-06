package messaging_server.client.consumer;

import messaging_server.client.config.DefaultConfig;
import messaging_server.models.SimpleMessage;
import messaging_server.rabbitMQ.Consumer;
import messaging_server.structures.SafeQueue;

import java.io.IOException;

public class PartnersMessagesConsumer extends Consumer {
    SafeQueue<SimpleMessage> receivedMessages = new SafeQueue<>();

    public PartnersMessagesConsumer(String queueName, String partnerId) {
        super(queueName);
        this.partnerId = partnerId;
    }

    private String partnerId;


    @Override
    protected void listener() {
        try {
            this.consumerTag = channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                SimpleMessage jsonMessage = objectMapper.readValue(message.getBody(), SimpleMessage.class);
                if (receivedMessages.exportAsList().size() >= DefaultConfig.nrMaxOfMessagesAccepted) {
                    receivedMessages.pop();
                }
                receivedMessages.add(jsonMessage);
            }, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();
        }
    }

    public void displayMessages() {
        SimpleMessage simpleMessage = receivedMessages.pop();

        if (simpleMessage != null) {
            System.out.print(simpleMessage.getMessageSender() + ": ");
            System.out.println(simpleMessage.getMessage());
        }
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerId() {
        return partnerId;
    }
}
