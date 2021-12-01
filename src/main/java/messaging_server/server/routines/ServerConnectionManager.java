package messaging_server.server.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.MessageResponse;
import messaging_server.rabbitMQ.RabbitMQConstants;
import messaging_server.server.data.ServerData;
import messaging_server.server.models.MessageToSend;

public class ServerConnectionManager extends ServerRoutine {
    @Override
    protected void routine() {
        SimpleEventMessage message = ServerData.incomingConnectionRequests.popExisting();

        String clientId = message.getMessageSender();
        String clientQueue = message.getMessage();

        SimpleEventMessage response = new SimpleEventMessage();

        response.setMessageSender(RabbitMQConstants.serverId);
        response.setMessageReceiver(clientId);

        if(!ServerData.connectedClients.exists(clientId)) {
            System.out.println("Connection Accepted");
            response.setEventType(MessageEvents.connectionRequestResponseSuccessful);
            response.setMessage(MessageResponse.connectionSuccessful);
            ServerData.addClient(clientId, clientQueue);
        } else {
            System.out.println("Connection Declined");
            response.setEventType(MessageEvents.connectionRequestResponseFailed);
            response.setMessage(MessageResponse.connectionFailed);
        }

        ServerData.messagesToSend.add(new MessageToSend(response, clientQueue));
    }
}
