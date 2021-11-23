package messaging_server.client;

import messaging_server.App;
import messaging_server.ConnectionManager;
import messaging_server.Consumer;
import messaging_server.Sender;

import java.util.concurrent.TimeUnit;

public class Client {
    public static void clientRoutine() throws InterruptedException {
        Consumer consumer1 = new Consumer("hello-queue-1", "con1");
        Consumer consumer2 = new Consumer("hello-queue-2", "con2");
        Consumer consumer3 = new Consumer("hello-queue-3", "con3");
        consumer1.startThread();
        consumer2.startThread();
        consumer3.startThread();
        Sender sender = new Sender();
        int counter = 0;
        while(true) {
            sender.sendMessageOnSimpleQueue("client" + counter, "server-receiver");
            counter += 1;
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
