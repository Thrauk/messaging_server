package messaging_server.client.utility;

import messaging_server.client.data.ClientData;
import messaging_server.models.JsonObject;
import messaging_server.rabbitMQ.TopicConsumer;
import messaging_server.rabbitMQ.TopicProducer;

public class ClientTopicOperations {
    public static void subscribeToTopic(String topicName) {
        if (ClientData.topicSubscriptions.exists(topicName)) {
            System.out.println("Already subscribed to this topic -> " + topicName);
        } else {
            TopicConsumer subscription = new TopicConsumer(topicName);
            ClientData.topicSubscriptions.add(topicName,subscription);
            subscription.thread.start();
        }
    }
    public static void publishToTopic(String topicName, JsonObject message)
    {
        TopicProducer publisher;
        if(ClientData.topicPublishers.exists(topicName))
        {
            publisher=ClientData.topicPublishers.get(topicName);
            publisher.publishMessage(message);
        }
        else
        {
            publisher=new TopicProducer(topicName);
            ClientData.topicPublishers.add(topicName,publisher);
            publisher.publishMessage(message);
        }
    }
}
