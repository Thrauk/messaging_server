package messaging_server.server.data;

import messaging_server.models.JsonObject;
import messaging_server.models.SimpleMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SafeQueue<T> {
    public final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public void add(T element) {
        queue.add(element);
    }


    // Returns head of queue and removes it from the queue. If no element is present, this method will listen until an element is added
    public T popExisting() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Returns head of queue if it exists. If it doesn't, null will be returned.
    public T pop() {
        if (this.queue.size() > 0) {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void displayInConsole() {
        queue.forEach(System.out::println);
    }

}
