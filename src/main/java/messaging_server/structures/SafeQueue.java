package messaging_server.structures;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SafeQueue<T> {
    protected final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    // Add an element to the back of the queue
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

    // Remove a specific element from the queue
    public boolean removeElement(T element) {
        return this.queue.remove(element);
    }

    // Check if element exists in queue
    public boolean exists(T element) {
        return queue.contains(element);
    }

    // Export a copy of the current state of the queue as a list
    public ArrayList<T> exportAsList() {
        return new ArrayList<>(queue);
    }

    // Prints the current queue in console line by line
    public void displayInConsole() {
        queue.forEach(System.out::println);
    }

}
