package messaging_server;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import messaging_server.client.Client;
import messaging_server.server.Server;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class App {
    public static void main(String [] args) {




        Server server = new Server();
        ConnectionManager.createConnection();
        Thread serverThread = new Thread(() -> {
            try {
                server.serverRoutine();
            } catch(InterruptedException v) {
                System.out.println(v);
            }
        });
        Thread clientThread = new Thread(() -> {
            try {
                Client.clientRoutine();
            } catch(InterruptedException v) {
                System.out.println(v);
            }
        });
        serverThread.start();
        clientThread.start();
    }


}
