package messaging_server.server.producer;

import messaging_server.Sender;
import messaging_server.models.SimpleMessage;
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
        while(true) {
            synchronized (serverData.messagesToSend) {
                if(serverData.messagesToSend.size() > 0) {
                    SimpleMessage sm = serverData.messagesToSend.get(0);
                    serverData.messagesToSend.remove(0);

                }
            }

        }
    }


    public void sendJsonOnSimpleQueue(SimpleMessage sm, String queueName) {
        //message += " " + LocalDateTime.now();
        try {
            this.channel.basicPublish("", queueName, false, null, message.getBytes());
            //System.out.println("sent message " + message);
        } catch (IOException e) {
            System.out.println("An error has occured while trying to send a message on " + queueName + " queue");
        }
    }
}
