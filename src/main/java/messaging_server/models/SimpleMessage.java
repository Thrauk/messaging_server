package messaging_server.models;

import java.io.IOException;

public class SimpleMessage extends JsonObject {
    private String messageSender;
    private String messageReceiver;
    private String message;

    public SimpleMessage(byte[] bytes)
    {
        try {
            SimpleMessage sm;
            sm=objectMapper.readValue(bytes, SimpleMessage.class);
            this.messageSender=sm.getMessageSender();
            this.messageReceiver=sm.getMessageReceiver();
            this.message=sm.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public SimpleMessage(String ms, String mr, String m)
    {
        this.messageSender=ms;
        this.messageReceiver=mr;
        this.message=m;
    }
    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String toString() {
        return messageSender + " --> " + messageReceiver + "\n: " + message + "\n";
    }
}
