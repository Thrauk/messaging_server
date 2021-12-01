package messaging_server.server.models;

import messaging_server.models.SimpleEventMessage;
import messaging_server.server.data.ServerData;

public class MessageToSend {
    public MessageToSend(SimpleEventMessage message) {
        this.message = message;
        this.queue = ServerData.getConnectedClientQueue(message.getMessageReceiver());
    }

    public MessageToSend(SimpleEventMessage message, String queue) {
        this.message = message;
        this.queue = queue;
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
