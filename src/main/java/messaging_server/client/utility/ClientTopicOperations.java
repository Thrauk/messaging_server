package messaging_server.client.utility;

import messaging_server.client.config.DefaultConfig;
import messaging_server.client.data.ClientData;
import messaging_server.models.JsonObject;
import messaging_server.rabbitMQ.TopicConsumer;
import messaging_server.rabbitMQ.TopicProducer;

public class ClientTopicOperations {
    public static void subscribeToTopic(String topicName) {

            TopicConsumer subscription = new TopicConsumer(topicName);
            ClientData.topicSubscriptions.add(topicName,subscription);
            subscription.thread.start();

    }
    public static void publishToTopic(String topicName, JsonObject message)
    {
        TopicProducer publisher;
        if(ClientData.topicPublishers.exists(topicName))
        {
            publisher=ClientData.topicPublishers.get(topicName);
        }
        else
        {
            publisher=new TopicProducer(topicName, DefaultConfig.topicMessageTTL);
            ClientData.topicPublishers.add(topicName,publisher);
        }
        publisher.publishMessage(message);
    }
}
