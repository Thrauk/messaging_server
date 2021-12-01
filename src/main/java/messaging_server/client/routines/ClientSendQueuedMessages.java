package messaging_server.client.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.JsonMessageProducer;
import messaging_server.rabbitMQ.RabbitMQConstants;
import messaging_server.server.data.ServerData;

public class ClientSendQueuedMessages extends ClientRoutine {
    private final JsonMessageProducer jsonMessageProducer = new JsonMessageProducer();

    @Override
    protected void routine() {
        String receivingQueue = null;
        SimpleEventMessage message = ServerData.messagesToSend.popExisting().getMessage();
        String receiver = message.getMessageReceiver();

        if(receiver.equals("server")) {
            receivingQueue = RabbitMQConstants.serverReceivingQueue;
        }

        if(receivingQueue != null){
            System.out.println("Sent message on queue " + receivingQueue);
            jsonMessageProducer.sendEventMessageOnQueue(message, receivingQueue);
        }
    }
}
