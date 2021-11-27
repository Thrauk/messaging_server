package messaging_server.server.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.rabbitMQ.Producer;
import messaging_server.models.SimpleMessage;
import messaging_server.server.data.ServerData;

import java.io.IOException;

public class ServerMessageProducer extends Producer {

    public final Thread thread = new Thread(this::sendQueuedMessages);

    public void sendQueuedMessages() {
        while (!thread.isInterrupted()) {
            SimpleMessage sm = ServerData.messagesToSend.popExisting();
            System.out.println("Sent message on queue " + sm.getMessageReceiver());
            sendJsonOnSimpleQueue(sm, sm.getMessageReceiver() + "-receiver");
        }
    }


    public void sendJsonOnSimpleQueue(SimpleMessage sm, String queueName) {
        //message += " " + LocalDateTime.now();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String sm_string = objectMapper.writeValueAsString(sm);
            this.channel.basicPublish("", queueName, false, null, sm_string.getBytes());
            //System.out.println("sent message " + message);
        } catch (IOException e) {
            System.out.println("An error has occured while trying to send a message on " + queueName + " queue");
        }
    }
}
