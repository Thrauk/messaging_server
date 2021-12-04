package messaging_server.server.models.json;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.RabbitMQConstants;

public class DisconnectedPartner extends SimpleEventMessage {
    public DisconnectedPartner(String partnerId, String receiverId) {
        this.setMessage(partnerId);
        this.setEventType(MessageEvents.disconnectedPartner);
        this.setMessageSender(RabbitMQConstants.serverId);
        this.setMessageReceiver(receiverId);
    }
}
