package messaging_server.server;

import messaging_server.rabbitMQ.RabbitMQConstants;
import messaging_server.server.consumer.ServerMainConsumer;
import messaging_server.server.routines.*;

import java.util.ArrayList;
import java.util.List;


public class Server {

    public Server() {
    }

    private final List<Thread> threads = new ArrayList<>();

    public void serverTestRoutine() {

        serverInitializingRoutine();

        //System.out.println("Server started!");

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

    public void serverInitializingRoutine()
    {
        System.out.println("Server initializing");

        //Initializing server's sending queue
        ServerSendQueuedMessages serverSendQueuedMessages = new ServerSendQueuedMessages();
        threads.add(serverSendQueuedMessages.thread);

        //Initializing server's reading messages ability
        ServerMainConsumer serverMainConsumer = new ServerMainConsumer(RabbitMQConstants.serverReceivingQueue);
        threads.add(serverMainConsumer.thread);

        //Initializing server's connection management of clients
        ServerConnectionManager serverConnectionManager = new ServerConnectionManager();
        threads.add(serverConnectionManager.thread);

        //Initializing server's event manager
        ServerEventManager serverEventManager = new ServerEventManager();
        threads.add(serverEventManager.thread);

        //Initializing server's console commands
        ServerConsole serverConsole = new ServerConsole();
        threads.add(serverConsole.thread);

        ServerHeartbeatCheckRoutine serverHeartbeatCheckRoutine = new ServerHeartbeatCheckRoutine();
        threads.add(serverHeartbeatCheckRoutine.thread);

        //Starting all initialized threads
        for(Thread thread : threads) {
            thread.start();
        }

    }

    public void serverStoppingRoutine()
    {
        for(Thread thread : threads) {
            thread.interrupt();
        }
    }

}
