package messaging_server.rabbitMQ;

import com.rabbitmq.client.AMQP;
import messaging_server.models.JsonHelper;
import messaging_server.models.JsonObject;

import java.io.IOException;

public class TopicProducer extends Producer {
    private String topicName;
    private int TTL = RabbitMQConstants.defaultTTL; // TIME TO LIVE IN MS

    public TopicProducer(String EXCHANGE_NAME) {
        super();
        this.topicName = EXCHANGE_NAME;
        try {
            channel.exchangeDeclare(this.topicName, "fanout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TopicProducer(String EXCHANGE_NAME, int TTL) {
        super();
        this.topicName = EXCHANGE_NAME;
        this.TTL = TTL;
        try {
            channel.exchangeDeclare(this.topicName, "fanout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(JsonObject message) {
        byte[] messageBodyBytes = "Hello. world!".getBytes();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration(Integer.toString(TTL)).build();
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
