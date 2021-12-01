package messaging_server.server.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.server.data.ServerData;
import messaging_server.rabbitMQ.JsonMessageProducer;

public class ServerSendQueuedMessages extends ServerRoutine {

    private final JsonMessageProducer serverMessageProducer = new JsonMessageProducer();

    @Override
    protected void routine() {
        SimpleEventMessage message = ServerData.messagesToSend.popExisting();
        String receivingQueue = ServerData.getConnectedClientQueue(message.getMessageReceiver());
        if(receivingQueue != null){
            System.out.println("Sent message on queue " + receivingQueue);
            serverMessageProducer.sendEventMessageOnQueue(message, receivingQueue);
        }
    }
}
