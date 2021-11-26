package messaging_server.client;

import messaging_server.App;
import messaging_server.ConnectionManager;
import messaging_server.Consumer;
import messaging_server.Sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


public class Client {

    private String clientName;

    public void consoleRoutine() throws IOException {

        System.out.println("Enter a name for the client: ");
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        clientName = reader.readLine();

        System.out.println("This is client '"+clientName+"' and it can do the following: ");
        System.out.println("1) Send a message to other clients.");
        System.out.println("2) Read/Subscribe to a specific topic.");
        System.out.println("3) Publish on a topic.");
        System.out.println("What do you want to do? Enter an option:");

        int selectedOption = 2;

        do{
            if(selectedOption < 1 || selectedOption > 3)
            {
                System.out.println("Selected option is not available, please choose again: ");
            }
            selectedOption = Integer.parseInt(reader.readLine());
        }while(selectedOption < 1 || selectedOption > 3);

    }

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
