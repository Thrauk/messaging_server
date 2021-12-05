package messaging_server.server.models;

import messaging_server.models.ListMessage;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.server.data.ServerData;

import java.util.List;

public class MessageToSend {
    public MessageToSend(SimpleEventMessage message) {
        this.message = message;
        this.queue = ServerData.getConnectedClientQueue(message.getMessageReceiver());
    }

    public MessageToSend(SimpleEventMessage message, String queue) {
        this.message = message;
        this.queue = queue;
    }

    public MessageToSend(ListMessage messageList)
    {
        SimpleEventMessage simpleEventMessage = new SimpleEventMessage();

        simpleEventMessage.setMessage(messageList.getMessageAsString());
        simpleEventMessage.setMessageSender(messageList.getMessageSender());
        simpleEventMessage.setEventType(messageList.getEventType());
        simpleEventMessage.setMessageReceiver(messageList.getMessageReceiver());

        this.message = simpleEventMessage;
        this.queue = ServerData.getConnectedClientQueue(simpleEventMessage.getMessageReceiver());
    }

    private final SimpleEventMessage message;
    private final String queue;

    public SimpleEventMessage getMessage() {
        return message;
    }

    public String getQueue() {
        return queue;
    }

}
