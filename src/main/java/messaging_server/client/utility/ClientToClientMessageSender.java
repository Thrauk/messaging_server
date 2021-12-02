package messaging_server.client.utility;

import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleMessage;
import messaging_server.rabbitMQ.JsonMessageProducer;

public class ClientToClientMessageSender {
    private static final JsonMessageProducer jsonMessageProducer = new JsonMessageProducer();

    public static void sendMessage(String queue, String receiver, String messageToSend) {
        SimpleMessage message = new SimpleMessage();
        message.setMessageSender(ClientData.clientId);
        message.setMessageReceiver(receiver);
        message.setMessage(messageToSend);
        jsonMessageProducer.sendJsonOnSimpleQueue(message, queue);
    }
}
