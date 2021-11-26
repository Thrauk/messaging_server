package messaging_server.models;

public class JsonExample {
    private String messageSender;
    private String messageReceiver;
    private String message;

    public JsonExample(String ms, String mr, String m)
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
