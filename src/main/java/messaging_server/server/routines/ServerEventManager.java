package messaging_server.server.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.MessageResponse;
import messaging_server.server.data.ServerData;
import messaging_server.server.config.DefaultConfig;

public class ServerEventManager extends ServerRoutine{
    @Override
    protected void routine() {
        SimpleEventMessage message = ServerData.incomingMessages.popExisting();

        String eventType = message.getEventType();

        if(eventType.equals(MessageEvents.checkIfConnected)) {
            checkIfConnected(message);
        }

    }

    private void checkIfConnected(SimpleEventMessage message) {
        String clientId = message.getMessageSender();
        String checkId = message.getMessage();

        String responseBody;

        if(ServerData.connectedClients.exists(checkId)) {
            responseBody = MessageResponse.clientConnected;
        } else {
            responseBody = MessageResponse.clientNotConnected;
        }

        SimpleEventMessage response = new SimpleEventMessage();
        response.setMessageSender(DefaultConfig.serverName);
        response.setMessageReceiver(clientId);
        response.setMessage(responseBody);

        ServerData.messagesToSend.add(response);
    }

}
