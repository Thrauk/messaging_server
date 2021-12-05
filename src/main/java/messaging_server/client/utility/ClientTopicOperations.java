package messaging_server.client.utility;

import messaging_server.client.config.DefaultConfig;
import messaging_server.client.data.ClientData;
import messaging_server.models.JsonObject;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.RabbitMQConstants;
import messaging_server.rabbitMQ.TopicConsumer;
import messaging_server.rabbitMQ.TopicProducer;
import messaging_server.server.data.ServerData;

public class ClientTopicOperations {
    public static void subscribeToTopic(String topicName) {

            TopicConsumer subscription = new TopicConsumer(topicName);
            ClientData.topicSubscriptions.add(topicName,subscription);
            subscription.thread.start();
            SimpleEventMessage sem= new SimpleEventMessage();
            sem.setEventType(MessageEvents.userSubscribeToTopic);
            sem.setMessage(topicName);
            sem.setMessageReceiver(RabbitMQConstants.serverId);
            sem.setMessageSender(ClientData.clientId);
            ClientData.messagesToSend.add(sem);

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
            publisher=new TopicProducer(topicName);
            ClientData.topicPublishers.add(topicName,publisher);
        }
        publisher.publishMessage(message);
    }
}
