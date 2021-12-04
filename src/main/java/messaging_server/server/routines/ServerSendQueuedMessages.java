package messaging_server.server.routines;

import messaging_server.models.JsonHelper;
import messaging_server.models.SimpleEventMessage;
import messaging_server.server.data.ServerData;
import messaging_server.rabbitMQ.JsonMessageProducer;
import messaging_server.server.models.MessageToSend;

public class ServerSendQueuedMessages extends ServerRoutine {

    private final JsonMessageProducer serverMessageProducer = new JsonMessageProducer();

    @Override
    protected void routine() {
        MessageToSend messageToSend = ServerData.messagesToSend.popExisting();
        SimpleEventMessage jsonMessage = messageToSend.getMessage();
        String receivingQueue = messageToSend.getQueue();
        if(receivingQueue != null){
            // System.out.println("Message to send "+ JsonHelper.stringify(messageToSend.getMessage()));
            // System.out.println("Sent message on queue " + receivingQueue);
            serverMessageProducer.sendEventMessageOnQueue(jsonMessage, receivingQueue);
        }
    }
}
