package messaging_server.rabbitMQ;

import com.rabbitmq.client.AMQP;
import messaging_server.models.JsonHelper;
import messaging_server.models.JsonObject;
import messaging_server.client.config.DefaultConfig;

import java.io.IOException;

public class TopicProducer extends Producer {
    private String topicName;


    public TopicProducer(String EXCHANGE_NAME) {
        super();
        this.topicName = EXCHANGE_NAME;
        try {
            channel.exchangeDeclare(this.topicName, "fanout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void publishMessage(JsonObject message) {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration(Integer.toString(DefaultConfig.topicMessageTTL)).build();
        try {
            channel.basicPublish(topicName, "", properties, JsonHelper.getBytes(message));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
