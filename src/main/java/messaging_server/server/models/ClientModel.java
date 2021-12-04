package messaging_server.server.models;

import messaging_server.server.config.DefaultConfig;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class ClientModel {
    public ClientModel() {
        clientId = "";
        receivingQueue = "";
        lastMessageReceived = LocalDateTime.now();
    }

    public ClientModel(String clientId) {
        this.clientId = clientId;
        this.receivingQueue = clientId + "-receiving";
    }

//    public ClientModel(ClientModel clientToCopy) {
//        this.clientId = clientToCopy.clientId;
//        this.receivingQueue = clientToCopy.receivingQueue;
//        this.lastMessageReceived = clientToCopy.lastMessageReceived;
//        this.timeout = clientToCopy.timeout;
//        this.maximumMessageQueue = clientToCopy.maximumMessageQueue;
//    }

    public ClientModel(String clientId, String receivingQueue) {
        this.clientId = clientId;
        this.receivingQueue = receivingQueue;
        lastMessageReceived = LocalDateTime.now();
    }

    private String clientId;
    private String receivingQueue;
    private LocalDateTime lastMessageReceived;
    private int timeout = DefaultConfig.clientTimeout;
    private int maximumMessageQueue = DefaultConfig.maxMsgQueue;
    private final List<String> partners = new ArrayList<>();

    public long getTimeDifference() {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.SECONDS.between(lastMessageReceived, now);
    }

    public boolean checkAlive() {
        return getTimeDifference() < timeout;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getReceivingQueue() {
        return receivingQueue;
    }

    public void setReceivingQueue(String receivingQueue) {
        this.receivingQueue = receivingQueue;
    }

    public LocalDateTime getLastMessageReceived() {
        return lastMessageReceived;
    }

    public void setLastMessageReceived(LocalDateTime lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaximumMessageQueue() {
        return maximumMessageQueue;
    }

    public void setMaximumMessageQueue(int maximumMessageQueue) {
        this.maximumMessageQueue = maximumMessageQueue;
    }

    public void addPartner(String partnerId) {
        if(!this.partners.contains(partnerId)){
            this.partners.add(partnerId);
        }
    }

    public void removePartner(String partnerId) {
        this.partners.remove(partnerId);
    }

    public boolean hasPartner(String partnerId) {
        return this.partners.contains(partnerId);
    }

}
