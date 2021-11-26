package messaging_server.server.data;

import com.rabbitmq.client.DeliverCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerData {
    public final List<String> connectedClients = Collections.synchronizedList(new ArrayList<String>());

    public final List<String> messagesToSend = Collections.synchronizedList(new ArrayList<String>());


    public void displayConnectedClients() {
        System.out.println("Connected clients: ");
        synchronized (connectedClients) {
            connectedClients.forEach(System.out::println);
        }

    }



}
