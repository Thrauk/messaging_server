package messaging_server.server.models;

import messaging_server.server.config.DefaultConfig;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


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

    public ClientModel(String clientId, String receivingQueue) {
        this.clientId = clientId;
        this.receivingQueue = receivingQueue;
    }

    private String clientId;
    private String receivingQueue;
    private LocalDateTime lastMessageReceived;
    private int timeout = DefaultConfig.clientTimeout;
    private int maximumMessageQueue = DefaultConfig.maxMsgQueue;

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
}
