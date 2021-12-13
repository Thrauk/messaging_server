package messaging_server.server.routines;

import messaging_server.models.ListMessage;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.RabbitMQConstants;
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
        } else if (eventType.equals(MessageEvents.userSubscribeToTopic)) {
            String topicName = message.getMessage();
            String clientID = message.getMessageSender();
            if (ServerData.topicAvailable.exists(topicName)) {
                ServerData.topicAvailable.get(topicName).add(clientID);
            } else {
                SafeQueue<String> subscribers = new SafeQueue<>();
                subscribers.add(clientID);
                ServerData.topicAvailable.add(topicName, subscribers);
            }
        }
        //if a message is sent on a topic the server receives an event with the topicName and adds it to its list of Topics
        if (eventType.equals(MessageEvents.messageOnTopic)) {
            String topicName = message.getMessage();
            if (!ServerData.topicAvailable.exists(topicName)) {
                ServerData.topicAvailable.add(topicName,new SafeQueue<String>());
            } else {
                System.out.println("Message posted on [" + topicName + "] topic.");
            }
        }


        if (eventType.equals(MessageEvents.requestConnectedClientsList)) {

            ListMessage connectedClientsMessage = new ListMessage(RabbitMQConstants.serverId, message.getMessageSender(), ServerData.connectedClients.exportKeysAsList(), MessageEvents.receiveConnectedClientsList);
            ServerData.messagesToSend.add(new MessageToSend(connectedClientsMessage));
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
