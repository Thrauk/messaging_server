package messaging_server.server;

import messaging_server.rabbitMQ.Consumer;
import messaging_server.rabbitMQ.Producer;
import messaging_server.server.consumer.ServerMainConsumer;
import messaging_server.server.data.ServerData;
import messaging_server.server.producer.ServerMessageProducer;
import messaging_server.server.routines.ServerListenForMessages;
import messaging_server.server.routines.ServerRoutine;

import java.util.concurrent.TimeUnit;

public class Server {

    public void serverTestRoutine() {
        System.out.println("Server Started");
        ServerRoutine serverListenForMessages = new ServerListenForMessages();
        serverListenForMessages.thread.start();
        while(true) {

        }
    }

}
