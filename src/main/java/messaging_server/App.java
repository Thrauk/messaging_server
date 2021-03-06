package messaging_server;

import messaging_server.client.Client;
import messaging_server.rabbitMQ.ConnectionManager;
import messaging_server.server.Server;

import java.io.IOException;

public class App {
    public static void main(String [] args) {

        switch (args[0].toLowerCase()){

            case "client":clientMain();break;

            case "server":serverMain();break;

            case "server-test":testServerMain();break;

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

        Client client = new Client();

        ConnectionManager.createConnection();

        client.thread.start();

    }

    public static void serverMain()
    {
        Server server = new Server();
        ConnectionManager.createConnection();

        Thread serverThread = new Thread(server::serverInitializingRoutine);

        serverThread.start();
    }

    public static void testServerMain()
    {
        Server server = new Server();
        ConnectionManager.createConnection();

        Thread serverThread = new Thread(server::serverInitializingRoutine);
        serverThread.start();
    }


}
