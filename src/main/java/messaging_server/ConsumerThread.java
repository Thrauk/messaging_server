package messaging_server;

public class ConsumerThread extends Thread{

    public ConsumerThread(Consumer consumer) {
        super();
        this.consumer = consumer;
    }

    private Consumer consumer;

    public void run() {
        consumer.listenSimpleQueue();
    }
}
