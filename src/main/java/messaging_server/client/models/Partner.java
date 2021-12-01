package messaging_server.client.models;

public class Partner {
    public Partner() {
        this.partnerId = "";
        this.receivingQueue = "";
        this.sendingQueue = "";
    }

    public Partner(String partnerId, String receivingQueue, String sendingQueue) {
        this.partnerId = partnerId;
        this.receivingQueue = receivingQueue;
        this.sendingQueue = sendingQueue;
    }

    private String partnerId;
    private String receivingQueue;
    private String sendingQueue;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getReceivingQueue() {
        return receivingQueue;
    }

    public void setReceivingQueue(String receivingQueue) {
        this.receivingQueue = receivingQueue;
    }

    public String getSendingQueue() {
        return sendingQueue;
    }

    public void setSendingQueue(String sendingQueue) {
        this.sendingQueue = sendingQueue;
    }
}
