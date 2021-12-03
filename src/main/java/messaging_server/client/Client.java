package messaging_server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
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

    private String clientName;

    static int selectedOption;

    public static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    public void consoleRoutine() throws IOException {

        System.out.println("Enter a name for the client: ");
        clientName = reader.readLine();

        ClientData.clientId = clientName;
        ClientData.setReceivingQueueServerClient();

        ClientServerMessageSender.sendConnectionRequest();

        ServerMessagesListener serverMessagesListener = new ServerMessagesListener(ClientData.receivingQueueServerClient);
        serverMessagesListener.thread.start();

        for (var consumer : ClientData.partnersMessagesConsumers) {
            consumer.thread.start();
        }

        //System.out.flush();

        System.out.println("Waiting for server response...");

        while (!ClientData.isConnected.get()) {
        }

        System.out.println("This is client '" + clientName + "' and it can do the following: ");
//        System.out.println("1) Send a message to other clients.");
//        System.out.println("2) Read/Subscribe to a specific topic.");
//        System.out.println("3) Publish on a topic.");
//        System.out.println("What do you want to do? Enter an option:");

        showMenu();

//        int selectedOption;
//
//        do{
//            selectedOption = Integer.parseInt(reader.readLine());
//            if(selectedOption < 1 || selectedOption > 3)
//            {
//                System.out.println("Selected option is not available, please choose again: ");
//            }
//            if(selectedOption == 2) {
//                System.out.println("Write client's name:");
//                String partnerName = reader.readLine();
//                ClientServerMessageSender.sendCheckIfPartnerConnected(partnerName);
//
//            }
//        }while(selectedOption < 1 || selectedOption > 3);

    }

    public static void showMenu() throws IOException {
        do {
            System.out.println("1) Send a message to other clients.");
            System.out.println("2) Read/Subscribe to a specific topic.");
            System.out.println("3) Publish on a topic.");
            System.out.println("What do you want to do? Enter an option:");

            selectedOption = Integer.parseInt(reader.readLine());
            if (selectedOption < 1 || selectedOption > 3) {
                System.out.println("Selected option is not available, please choose again: ");
            }
            if (selectedOption == 1) {
                System.out.println("Write client's name:");
                String partnerName = reader.readLine();
                ClientServerMessageSender.sendCheckIfPartnerConnected(partnerName);
            }
            if (selectedOption==2)
            {
                System.out.println("Write topic name: ");
                String topicName=reader.readLine();

                if(ClientData.topicSubscriptions.exists(topicName))
                {
                    TopicConsumer tc =ClientData.topicSubscriptions.get(topicName);
                    //TopicConsumer nume =ClientData.topicSubscriptions.get(topicName)
                    //metoda(nume.savedMessages)
                    /*
                           afisare de mesaje de pe topic iar in metoda  metoda facem pop de mesajele din lista pentru a afisa o singura data
                     */
                    tc.getMessagesFromTopic();
                }
                else
                {
                    ClientTopicOperations.subscribeToTopic(topicName);
                }
                showMenu();
            }
            if (selectedOption == 3) {
                System.out.println("Write topic's to subscribe name:");
                String topicName = reader.readLine();
                System.out.println("Write message: ");
                String message = reader.readLine();
                SimpleMessage sm=new SimpleMessage();
                sm.setMessage(message);
                sm.setMessageReceiver("");
                sm.setMessageSender(ClientData.clientId);
                ClientTopicOperations.publishToTopic(topicName, sm);
                showMenu();
            }
        } while (selectedOption < 1 || selectedOption > 3);
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
