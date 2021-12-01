package messaging_server.server.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.MessageResponse;
import messaging_server.server.data.ServerData;
import messaging_server.server.config.DefaultConfig;

public class ServerConnectionManager extends ServerRoutine {
    @Override
    protected void routine() {
        SimpleEventMessage message = ServerData.incomingConnectionRequests.popExisting();

        String clientId = message.getMessageSender();

        SimpleEventMessage response = new SimpleEventMessage();

        response.setMessageSender(DefaultConfig.serverName);
        response.setMessageReceiver(clientId);

        if(!ServerData.connectedClients.exists(clientId)) {
            response.setEventType(MessageEvents.connectionRequestResponseSuccessful);
            response.setMessage(MessageResponse.connectionSuccessful);
            ServerData.addClient(clientId);
        } else {
            response.setEventType(MessageEvents.connectionRequestResponseFailed);
            response.setMessage(MessageResponse.connectionFailed);
        }

        ServerData.messagesToSend.add(response);
    }
}
