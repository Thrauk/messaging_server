package messaging_server.client.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.Consumer;
import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleMessage;
import messaging_server.rabbitMQ.MessageEvents;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

                if(!ClientData.isConnected.get()) {
                    if(jsonMessage.getEventType().equals(MessageEvents.connectionRequestResponseSuccessful)) {
                        ClientData.isConnected.compareAndSet(false,true);
                        System.out.println("Client connected succ");
                    }
                    else {
                        System.out.println("Client connection failed!");
                    }
                }

            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();
        }
    }
}
