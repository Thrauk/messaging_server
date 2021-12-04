package messaging_server.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.models.JsonObject;
import messaging_server.models.SimpleEventMessage;
import messaging_server.models.SimpleMessage;
import messaging_server.server.data.ServerData;
import messaging_server.structures.SafeQueue;

import java.io.IOException;
import java.time.LocalDateTime;

public class TopicConsumer extends Consumer {
    private final String topicName;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public final SafeQueue<SimpleMessage> savedMessages=new SafeQueue<>();
    public TopicConsumer(String topicName) {
        super("");
        this.topicName = topicName;
    }

    @Override
    protected void listener() {

        try {
            channel.queueBind(queueName, topicName, "");
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                SimpleMessage jsonMessage = objectMapper.readValue(message.getBody(), SimpleMessage.class);
                //System.out.println("Message on topic (" + topicName + ") from: " + jsonMessage.getMessageSender() + "--> " + jsonMessage.getMessage());
                savedMessages.add(jsonMessage);
            }, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            //e.printStackTrace();

        }
    }
    public void getMessagesFromTopic()
    {
        SimpleMessage msg = savedMessages.pop();
        while(msg!=null)
        {
            System.out.println("Message: "+ msg.getMessage()+" received from: "+msg.getMessageSender()+" TOPIC: " + topicName);
            msg=savedMessages.pop();
        }
    }
}
