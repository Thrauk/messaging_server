package messaging_server.server.producer;

import messaging_server.Sender;
import messaging_server.server.data.ServerData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerQueueProducer  extends Sender {

    public ServerQueueProducer(ServerData serverData) {
        this.serverData = serverData;
    }

    private final ServerData serverData;
    private Thread senderThread = null;


    public void startThread() {
        if(this.senderThread == null) {
            setThreadRunnable(this::sendQueuedMessages);
        }
        this.senderThread.start();
    }

    public void stopThread() {
        this.senderThread.interrupt();
    }

    protected void setThreadRunnable(Runnable target) {
        this.senderThread = new Thread(target);
    }

    public void sendQueuedMessages() {

        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                String m = new String(message.getBody(), StandardCharsets.UTF_8);
                synchronized (serverData.connectedClients) {
                    serverData.connectedClients.add(m);
                }

            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();

        }

    }
}
