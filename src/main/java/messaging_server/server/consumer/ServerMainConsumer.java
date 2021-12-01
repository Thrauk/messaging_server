package messaging_server.server.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.Consumer;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.server.data.ServerData;

import java.io.IOException;
import java.time.LocalDateTime;

public class ServerMainConsumer extends Consumer {


    public ServerMainConsumer(String queueName) {
        super(queueName);
    }
    private final ObjectMapper objectMapper = new ObjectMapper();
    LocalDateTime timeReceived;

    @Override
    protected void listener() {
        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                System.out.println("Got message");

                timeReceived = LocalDateTime.now();


                SimpleEventMessage jsonMessage = objectMapper.readValue(message.getBody(), SimpleEventMessage.class);
                String sender_id = jsonMessage.getMessageSender();
                System.out.println("Sender is " + jsonMessage.getMessageSender());



                if(jsonMessage.getEventType().equals(MessageEvents.connectionRequest)) {
                    ServerData.incomingConnectionRequests.add(jsonMessage);
                } else if(ServerData.connectedClients.exists(sender_id)) {
                    ServerData.connectedClients.get(sender_id).setLastMessageReceived(timeReceived);
                    ServerData.incomingMessages.add(jsonMessage);
                }



            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();

        }
    }


}
