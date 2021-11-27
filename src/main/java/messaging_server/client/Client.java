package messaging_server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import messaging_server.App;
import messaging_server.ConnectionManager;
import messaging_server.Consumer;
import messaging_server.Sender;
import messaging_server.client.consumer.ServerMessagesListener;
import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleMessage;
import messaging_server.server.consumer.ServerMainConsumer;
import messaging_server.server.data.ServerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


public class Client {

    public Client() {
        this.clientData = new ClientData();
    }

    private String clientName;
    private final ClientData clientData;

    public void consoleRoutine() throws IOException {

        System.out.println("Enter a name for the client: ");
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        clientName = reader.readLine();

        SimpleMessage sm = new SimpleMessage();
        sm.setMessageSender(clientName);
        sm.setMessageReceiver("server");
        sm.setMessage("Conn req");

        this.sendJsonOnSimpleQueue(sm, "server-receiver");

        Thread clientListenServerMsgThread = new Thread(this::clientRoutineTest);
        clientListenServerMsgThread.start();

        System.out.flush();

        System.out.println("Waiting for server response...");

        while(!clientData.isConnected) {
        }

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

    public void clientRoutineTest() {
        ServerMessagesListener serverMessagesListener = new ServerMessagesListener(this.clientName + "-receiver", this.clientData);
        serverMessagesListener.startThread();
        while(true) {

        }
    }


    public void sendJsonOnSimpleQueue(SimpleMessage sm, String queueName) {
        //message += " " + LocalDateTime.now();
        Channel channel;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            channel = ConnectionManager.connection.createChannel();
            String sm_string = objectMapper.writeValueAsString(sm);
            channel.basicPublish("", queueName, false, null, sm_string.getBytes());
            //System.out.println("sent message " + message);
        } catch (IOException e) {
            System.out.println("An error has occured while trying to send a message on " + queueName + " queue");
        }
    }
}
