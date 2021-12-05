package messaging_server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import messaging_server.client.routines.ClientHeartbeat;
import messaging_server.client.routines.ClientRoutine;
import messaging_server.client.utility.ClientServerMessageSender;
import messaging_server.client.utility.ClientTopicOperations;
import messaging_server.models.JsonHelper;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.*;
import messaging_server.client.consumer.ServerMessagesListener;
import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


public class Client {

    private static String clientName = "";

    public static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    public Thread thread = new Thread(this::consoleRoutine);

    public void consoleRoutine() {

        System.out.println("Enter a name for the client: ");

        try
        {
            clientName = reader.readLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error while reading client's name");
        }

        ClientData.clientId = clientName;
        ClientData.setReceivingQueueServerClient();

        ClientServerMessageSender.sendConnectionRequest();

        ServerMessagesListener serverMessagesListener = new ServerMessagesListener(ClientData.receivingQueueServerClient);
        serverMessagesListener.thread.start();

        for (var consumer : ClientData.partnersMessagesConsumers.exportAsList()) {
            consumer.thread.start();
        }


        System.out.println("Waiting for server response...");

        while (!ClientData.isConnected.get()) {
        }

        ClientHeartbeat clientHeartbeat = new ClientHeartbeat();
        clientHeartbeat.thread.start();

    //    while(!this.thread.isInterrupted())
        {
            clientMenu();
        }

    }

    public void clientMenu()
    {
        showMenu();

        int selectedOption = 0;

        boolean valueOk = true;

        do{

            selectedOption = 0;

            try
            {
                selectedOption = Integer.parseInt(reader.readLine());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Reading error");
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
                System.out.println("Invalid character");
            }

            valueOk = true;

            switch(selectedOption)
            {
                case 1:
                {
                    sendMessageToOtherClients();
                }break;

                case 2:
                {
                    readSpecificTopic();
                }break;

                case 3:
                {
                    publishOnTopic();
                }break;

                case 4:
                {
                    configureTopicTTL();
                }break;

                default:
                {
                    System.out.println("Selected option is not available, please choose again: ");
                    showMenu();
                    valueOk = false;
                }
            }

        }while(!valueOk);


    }

    public static void showMenu() {

        System.out.println("This is client '" + clientName + "' and it can do the following: ");

        System.out.println("1) Send a message to other clients.");
        System.out.println("2) Read/Subscribe to a specific topic.");
        System.out.println("3) Publish on a topic.");
        System.out.println("4) Configure default topic message TTL");

        System.out.println("What do you want to do? Enter an option:");

    }

    public void sendMessageToOtherClients()
    {
        System.out.println("Write client's name:");
        String partnerName = null;

        try {

            partnerName = reader.readLine();

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Reading error");

        }

        ClientServerMessageSender.sendCheckIfPartnerConnected(partnerName);
    }

    public void readSpecificTopic()
    {
        System.out.println("Write topic name: ");
        String topicName= null;
        try {

            topicName = reader.readLine();

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Reading error");

        }

        if(ClientData.topicSubscriptions.exists(topicName))
        {
            TopicConsumer tc =ClientData.topicSubscriptions.get(topicName);
            tc.getMessagesFromTopic();
        }
        else
        {
            ClientTopicOperations.subscribeToTopic(topicName);
        }

    }

    public void publishOnTopic()
    {
        System.out.println("Write topic's to subscribe name:");
        String topicName = null;

        try {

            topicName = reader.readLine();

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Reading error");

        }

        System.out.println("Write message: ");
        String message = null;

        try {

            message = reader.readLine();

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Reading error");

        }

        SimpleMessage sm=new SimpleMessage();
        sm.setMessage(message);
        sm.setMessageReceiver("");
        sm.setMessageSender(ClientData.clientId);
        ClientTopicOperations.publishToTopic(topicName, sm);
    }

    public void configureTopicTTL()
    {
        //To Be Added
    }

    public void clientRoutineTest() {
        ServerMessagesListener serverMessagesListener = new ServerMessagesListener(this.clientName + "-receiver");
        serverMessagesListener.thread.start();
        while (true) {

        }
    }


    public void sendJsonOnSimpleQueue(SimpleMessage sm, String queueName) {
        //message += " " + LocalDateTime.now();
        Channel channel;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            channel = ConnectionManager.connection.createChannel();
            //String sm_string = objectMapper.writeValueAsString(sm);
            channel.basicPublish("", queueName, false, null, JsonHelper.getBytes(sm));
            //System.out.println("sent message " + message);
        } catch (IOException e) {
            System.out.println("An error has occured while trying to send a message on " + queueName + " queue");
        }
    }
}
