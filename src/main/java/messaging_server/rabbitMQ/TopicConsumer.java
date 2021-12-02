package messaging_server.rabbitMQ;

import messaging_server.models.SimpleEventMessage;
import messaging_server.server.data.ServerData;

import java.io.IOException;
import java.time.LocalDateTime;

public class TopicConsumer extends Consumer{
    private final String topicName;
    public TopicConsumer(String topicName)
    {
        super("");
        this.topicName=topicName;
    }
    @Override
    protected void listener() {

        try {
            channel.queueBind(queueName, topicName, "");
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {

            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();

        }
    }
}
