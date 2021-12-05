package messaging_server.client.models;

import messaging_server.client.consumer.PartnersMessagesConsumer;

public class Partner {
    public Partner() {
        this.partnerId = "";
        this.sendingQueue = "";
    }

    public Partner(String partnerId, PartnersMessagesConsumer partnersMessagesConsumer, String sendingQueue) {
        this.partnerId = partnerId;
        this.partnersMessagesConsumer = partnersMessagesConsumer;
        this.sendingQueue = sendingQueue;
    }

    public Partner(String partnerId, PartnersMessagesConsumer partnersMessagesConsumer) {
        this.partnerId = partnerId;
        this.partnersMessagesConsumer = partnersMessagesConsumer;
    }


    public Partner(String partnerId, String sendingQueue) {
        this.partnerId = partnerId;
        this.sendingQueue = sendingQueue;
    }

    private String partnerId;
    private PartnersMessagesConsumer partnersMessagesConsumer = null;
    private String sendingQueue;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }


    public String getSendingQueue() {
        return sendingQueue;
    }

    public void setSendingQueue(String sendingQueue) {
        this.sendingQueue = sendingQueue;
    }


    public PartnersMessagesConsumer getPartnersMessagesConsumer() {
        return partnersMessagesConsumer;
    }

    public void setPartnersMessagesConsumer(PartnersMessagesConsumer partnersMessagesConsumer) {
        if (this.partnersMessagesConsumer == null) {
            this.partnersMessagesConsumer = partnersMessagesConsumer;
        }
    }
}
