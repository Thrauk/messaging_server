package messaging_server.client.utility;

import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.JsonMessageProducer;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.RabbitMQConstants;

public class ClientServerMessageSender {
    private static final JsonMessageProducer jsonMessageProducer = new JsonMessageProducer();

    public static void sendConnectionRequest() {
        SimpleEventMessage message = new SimpleEventMessage();
        message.setMessageSender(ClientData.clientId);
        message.setMessageReceiver(RabbitMQConstants.serverId);
        message.setEventType(MessageEvents.connectionRequest);
        message.setMessage(ClientData.receivingQueueServerClient);
        jsonMessageProducer.sendEventMessageOnQueue(message, RabbitMQConstants.serverReceivingQueue);
    }

    public static void sendCheckIfPartnerConnected(String partnerUid) {
        SimpleEventMessage message = new SimpleEventMessage();
        message.setMessageSender(ClientData.clientId);
        message.setMessageReceiver(RabbitMQConstants.serverId);
        message.setEventType(MessageEvents.checkIfConnected);
        message.setMessage(partnerUid);
        jsonMessageProducer.sendEventMessageOnQueue(message, RabbitMQConstants.serverReceivingQueue);

    }

}
