package messaging_server;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class Consumer {

    public Consumer(String queueName) {
        try {
            this.channel = ConnectionManager.connection.createChannel();
            this.queueName = queueName;
            this.consumerName = "consumer_for_" + queueName;
            this.channel.queueDeclare(this.queueName, false, false, false, null);
        } catch (IOException e) {
            System.out.println("An error occurred while creating a Consumer!");
        }
    }

    public Consumer(String queueName, String consumerName) {
        try {
            this.channel = ConnectionManager.connection.createChannel();
            this.queueName = queueName;
            this.consumerName = consumerName;
            this.channel.queueDeclare(this.queueName, false, false, false, null);
        } catch (IOException e) {
            System.out.println("An error occurred while creating a Consumer!");
        }
    }

    protected Channel channel;
    protected String queueName;
    protected String consumerName;
    protected Thread consumerThread = null;

    public void startThread() {
        if(this.consumerThread == null) {
            setThreadRunnable(this::listenSimpleQueue);
        }
        this.consumerThread.start();
    }

    public void stopThread() {
        this.consumerThread.interrupt();
    }

    protected void setThreadRunnable(Runnable target) {
        this.consumerThread = new Thread(target);
    }

    public void listenSimpleQueue() {

        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                String m = new String(message.getBody(), StandardCharsets.UTF_8);
                //System.out.println("Consumer " + consumerName + " : " + m);

            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
        }

    }


}
