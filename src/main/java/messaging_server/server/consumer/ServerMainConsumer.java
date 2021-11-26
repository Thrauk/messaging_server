package messaging_server.server.consumer;

import messaging_server.Consumer;
import messaging_server.server.Server;
import messaging_server.server.data.ServerData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerMainConsumer extends Consumer {

    public ServerMainConsumer(String queueName, ServerData serverData) {
        super(queueName);
        this.serverData = serverData;
        this.setThreadRunnable(this::listenServerMainQueue);
    }

    private final ServerData serverData;

    public void listenServerMainQueue() {

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

    public void listenServerMainQueueTesting() {

        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                String m = new String(message.getBody(), StandardCharsets.UTF_8);
                if(serverData.connectedClients.contains(m)) {
                    synchronized (serverData.messagesToSend) {
                        serverData.messagesToSend.add(this.queue);
                    }
                }
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
