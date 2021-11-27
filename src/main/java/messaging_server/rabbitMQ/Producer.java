package messaging_server.rabbitMQ;

import com.rabbitmq.client.Channel;

import java.io.IOException;

public class Producer {

    public Producer() {
        try {
            this.channel = ConnectionManager.connection.createChannel();
        } catch (IOException e) {
            System.out.println("An error occurred while creating a Sender!");
        }

    }

    protected Channel channel;

    public void sendMessageOnSimpleQueue(String message, String queueName) {
        //message += " " + LocalDateTime.now();
        try {
            this.channel.basicPublish("", queueName, false, null, message.getBytes());
            //System.out.println("sent message " + message);
        } catch (IOException e) {
            System.out.println("An error has occured while trying to send a message on " + queueName + " queue");
        }
    }


    /*
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        /* We don't need these because we use the default data for RABBITMQ
        factory.setHost();
        factory.setUsername();

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
    }*/
}
