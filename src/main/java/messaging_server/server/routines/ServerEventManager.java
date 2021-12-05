package messaging_server.server.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.MessageResponse;
import messaging_server.server.data.ServerData;
import messaging_server.server.config.DefaultConfig;
import messaging_server.server.models.MessageToSend;
import messaging_server.structures.SafeQueue;

import java.util.Objects;

public class ServerEventManager extends ServerRoutine {
    @Override
    protected void routine() {
        SimpleEventMessage message = ServerData.incomingMessages.popExisting();

        String eventType = message.getEventType();

        if (eventType.equals(MessageEvents.checkIfConnected)) {
            checkIfConnected(message);
        }
        else if(eventType.equals(MessageEvents.userSubscribeToTopic)) {
            String topicName= message.getMessage();
            String clientID=message.getMessageSender();
            if(ServerData.topicSubscribers.exists(topicName))
            {
                ServerData.topicSubscribers.get(topicName).add(clientID);
            }
            else
            {
                SafeQueue<String> subscribers=new SafeQueue<>();
                subscribers.add(clientID);
                ServerData.topicSubscribers.add(topicName,subscribers);
            }
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
            String partnerId = message.getMessage();

            responseToPartner.setEventType(MessageEvents.listenForNewMessages);
            responseToPartner.setMessageSender(DefaultConfig.serverName);
            responseToPartner.setMessageReceiver(message.getMessage());
            responseToPartner.setMessage(message.getMessageSender() + "-" + message.getMessage());

            ServerData.messagesToSend.add(new MessageToSend(responseToPartner));


            // The two clients are connected as partners (does not matter which direction)
            //System.out.println("New partners are " + partnerId + " and " + clientId);
            ServerData.connectedClients.get(partnerId).addPartner(clientId);
            ServerData.connectedClients.get(clientId).addPartner(partnerId);
        }
    }
}
