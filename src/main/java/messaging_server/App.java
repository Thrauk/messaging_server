package messaging_server;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import messaging_server.client.Client;
import messaging_server.server.Server;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class App {
    public static void main(String [] args) {

        switch (args[0].toLowerCase()){

            case "client":clientMain();break;

            case "server":serverMain();break;

            default:System.out.println("Not a valid option.");

        }

    }

    public static void clientMain()
    {
      /*  Server server = new Server();
        ConnectionManager.createConnection();

        Thread clientThread = new Thread(() -> {
            try {
                Client.clientRoutine();
            } catch(InterruptedException v) {
                System.out.println(v);
            }
        });

        clientThread.start();
        */

        Server server = new Server();
        Client client = new Client();

        ConnectionManager.createConnection();

        Thread clientThread = new Thread(() -> {
            try {
                client.consoleRoutine();
            } catch(IOException v) {
                System.out.println(v);
            }
        });

        clientThread.start();

    }

    public static void serverMain()
    {
        Server server = new Server();
        ConnectionManager.createConnection();

        Thread serverThread = new Thread(() -> {
            try {
                server.serverRoutine();
            } catch(InterruptedException v) {
                System.out.println(v);
            }
        });

        serverThread.start();
    }


}
