package messaging_server.client.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.client.Client;
import messaging_server.client.data.ClientData;
import messaging_server.client.models.Partner;
import messaging_server.client.utility.ClientToClientMessageSender;
import messaging_server.models.ListMessage;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.Consumer;
import messaging_server.rabbitMQ.MessageEvents;

import java.io.IOException;
import java.util.List;

public class ServerDataListener extends Consumer {
    public ServerDataListener(String queueName) {
        super(queueName);
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void listener() {
        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                ListMessage jsonMessage = objectMapper.readValue(message.getBody(), ListMessage.class);

                if (ClientData.isConnected.get()) {

                    if (jsonMessage.getEventType().equals(MessageEvents.receiveConnectedClientsList)) {

                        List<String> receivedMessage = jsonMessage.getMessage();

                    }
                }
            }, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();
        }
    }


}
