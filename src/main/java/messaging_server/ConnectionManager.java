package messaging_server;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionManager {
    public static Connection connection;

    public static void createConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        /* We don't need these because we use the default data for RABBITMQ
        factory.setHost();
        factory.setUsername();
         */

        try {
            ConnectionManager.connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            System.out.println("An error occurred while connecting to RabbitMQ!");
        }
    }
}
