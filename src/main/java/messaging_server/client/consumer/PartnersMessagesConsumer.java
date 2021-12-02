package messaging_server.client.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.models.SimpleMessage;
import messaging_server.rabbitMQ.Consumer;
import java.io.IOException;

public class PartnersMessagesConsumer extends Consumer {
    public PartnersMessagesConsumer(String queueName) {
        super(queueName);
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void listener() {
        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                SimpleMessage jsonMessage = objectMapper.readValue(message.getBody(), SimpleMessage.class);
                System.out.print(jsonMessage.getMessageSender() + " sent you a message:");
                System.out.println(jsonMessage.getMessage());
                System.out.println();
            }, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();
        }
    }
}
