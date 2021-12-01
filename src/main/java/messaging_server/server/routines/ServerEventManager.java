package messaging_server.server.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.MessageResponse;
import messaging_server.server.data.ServerData;
import messaging_server.server.config.DefaultConfig;
import messaging_server.server.models.MessageToSend;

import java.util.Objects;

public class ServerEventManager extends ServerRoutine {
    @Override
    protected void routine() {
        SimpleEventMessage message = ServerData.incomingMessages.popExisting();

        String eventType = message.getEventType();

        if (eventType.equals(MessageEvents.checkIfConnected)) {
            checkIfConnected(message);
        }

    }

    private void checkIfConnected(SimpleEventMessage message) {
        String clientId = message.getMessageSender();
        String checkId = message.getMessage();

        String responseEvent;

        if (ServerData.connectedClients.exists(checkId)) {
            responseEvent = MessageEvents.checkIfConnectedResponseSuccessful;
        } else {
            responseEvent = MessageEvents.checkIfConnectedResponseFailed;
        }

        SimpleEventMessage responseToSender = new SimpleEventMessage();
        responseToSender.setEventType(responseEvent);
        responseToSender.setMessageSender(DefaultConfig.serverName);
        responseToSender.setMessageReceiver(clientId);
        responseToSender.setMessage(message.getMessageSender() + "-" + message.getMessage());
        ServerData.messagesToSend.add(new MessageToSend(responseToSender));

        if (Objects.equals(responseEvent, MessageEvents.checkIfConnectedResponseSuccessful)) {
            SimpleEventMessage responseToPartner = new SimpleEventMessage();
            responseToPartner.setEventType(MessageEvents.listenForNewMessages);
            responseToPartner.setMessageSender(DefaultConfig.serverName);
            responseToPartner.setMessageReceiver(message.getMessage());
            responseToPartner.setMessage(message.getMessageSender() + "-" + message.getMessage());
            ServerData.messagesToSend.add(new MessageToSend(responseToPartner));
        }
    }
}
