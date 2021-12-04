package messaging_server.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.io.IOException;

public abstract class Consumer {

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected String consumerTag = "";

    public Consumer(String queueName) {
        try {
            this.channel = ConnectionManager.connection.createChannel();
            this.queueName = queueName;
            this.channel.queueDeclare(this.queueName, false, false, false, null);
        } catch (IOException e) {
            System.out.println("An error occurred while creating a Consumer!");
        }
    }

    public Thread thread = new Thread(this::listener);
    protected Channel channel;
    protected String queueName;

    protected abstract void listener();

    public void closeListener() {
        try {
            this.channel.basicCancel(this.consumerTag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
