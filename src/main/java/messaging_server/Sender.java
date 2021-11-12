package messaging_server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class Sender {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        /* We don't need these because we use the default data for RABBITMQ
        factory.setHost();
        factory.setUsername();
         */
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("hello-world", false, false, false, null);

            String message = "is this the matrix? " + LocalDateTime.now();

            channel.basicPublish("", "hello-world", false, null, message.getBytes() );

            System.out.println("!!! message has been sent");

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
