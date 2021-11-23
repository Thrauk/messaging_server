package messaging_server.server;

import messaging_server.App;
import messaging_server.ConnectionManager;
import messaging_server.Consumer;
import messaging_server.Sender;
import messaging_server.server.consumer.ServerMainConsumer;
import messaging_server.server.data.ServerData;

import java.util.concurrent.TimeUnit;

public class Server {

    public Server() {
        this.serverData = new ServerData();
    }

    public void serverRoutine() throws InterruptedException {
        ServerMainConsumer serverMainConsumer = new ServerMainConsumer("server-receiver", this.serverData);
        serverMainConsumer.startThread();
        Sender sender = new Sender();
        Thread disconnectingThread = new Thread() {public void run() {
            try {
                removeClient();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }};
        disconnectingThread.start();
        while(true) {
            sender.sendMessageOnSimpleQueue("This is message", "hello-queue-1");
            sender.sendMessageOnSimpleQueue("This is message", "hello-queue-2");
            sender.sendMessageOnSimpleQueue("This is message", "hello-queue-3");
            this.serverData.displayConnectedClients();
            TimeUnit.SECONDS.sleep(2);
        }
    }

    private ServerData serverData;

    private void listenForClients() {
        Consumer connectionListener = new Consumer("server-receiver", "server-receiver");
        connectionListener.startThread();

    }

    private void removeClient() throws InterruptedException {
        int client_id = 0;
        String clientStr;
        while(true) {
            TimeUnit.SECONDS.sleep(2);
            clientStr = "client" + client_id;

            synchronized (serverData.connectedClients){

                if(this.serverData.connectedClients.contains(clientStr)) {
                    System.out.println("Disconnecting client");
                    this.serverData.connectedClients.remove(clientStr);
                    client_id += 1;
                }

            }
        }
    }

}
