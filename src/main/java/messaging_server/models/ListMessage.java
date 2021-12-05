package messaging_server.models;

import java.util.ArrayList;
import java.util.List;

public class ListMessage extends JsonObject{

    private String messageSender ;
    private String messageReceiver;
    private List<String> message;
    private String eventType;

    public ListMessage(String sender, String receiver, List<String> message, String event)
    {
        this.message = message;
        this.messageReceiver = receiver;
        this.messageSender = sender;
        this.eventType = event;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessageAsString()
    {
        String aux ="";

        for(String iter: message)
        {
            aux = aux +iter+'\n';
        }

        return aux;
    }
}
