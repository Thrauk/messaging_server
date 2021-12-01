package messaging_server.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.models.JsonHelper;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.Producer;
import messaging_server.models.SimpleMessage;
import messaging_server.server.data.ServerData;

import java.io.IOException;

public class JsonMessageProducer extends Producer {

    public void sendJsonOnSimpleQueue(SimpleMessage sm, String queueName) {
        //message += " " + LocalDateTime.now();
        try {
            //String sm_string = objectMapper.writeValueAsString(sm);
            this.channel.basicPublish("", queueName, false, null, JsonHelper.getBytes(sm));
            //System.out.println("sent message " + message);
        } catch (IOException e) {
            System.out.println("An error has occured while trying to send a message on " + queueName + " queue");
        }
    }

    public void sendEventMessageOnQueue(SimpleEventMessage message, String queueName) {
        try {
            this.channel.basicPublish("", queueName, false, null, JsonHelper.getBytes(message));
        } catch (IOException e) {
            System.out.println("An error has occured while trying to send a message on " + queueName + " queue");
        }
    }

}
