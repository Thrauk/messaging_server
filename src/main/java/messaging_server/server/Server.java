package messaging_server.server;

import messaging_server.rabbitMQ.RabbitMQConstants;
import messaging_server.server.consumer.ServerMainConsumer;
import messaging_server.server.routines.ServerConnectionManager;
import messaging_server.server.routines.ServerEventManager;
import messaging_server.server.routines.ServerSendQueuedMessages;

import java.util.ArrayList;
import java.util.List;


public class Server {



    public Server() {
    }

    private final List<Thread> threads = new ArrayList<>();

    public void serverTestRoutine() {
        System.out.println("Server initializing");

        ServerSendQueuedMessages serverSendQueuedMessages = new ServerSendQueuedMessages();
        threads.add(serverSendQueuedMessages.thread);
        //serverMessageSender.thread.start();



        ServerMainConsumer serverMainConsumer = new ServerMainConsumer(RabbitMQConstants.serverReceivingQueue);
        threads.add(serverMainConsumer.thread);

        ServerConnectionManager serverConnectionManager = new ServerConnectionManager();
        threads.add(serverConnectionManager.thread);

        ServerEventManager serverEventManager = new ServerEventManager();
        threads.add(serverEventManager.thread);

        for(Thread thread : threads) {
            thread.start();
        }

        System.out.println("Server started!");

        //serverListenForMessages.thread.start();

        try {
            for(Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Server Closed");
    }

}
