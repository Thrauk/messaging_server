package messaging_server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import messaging_server.client.config.DefaultConfig;
import messaging_server.client.models.Partner;
import messaging_server.client.routines.ClientHeartbeat;
import messaging_server.client.routines.DirectMessageDisplay;
import messaging_server.client.routines.TopicDisplayRoutine;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static messaging_server.rabbitMQ.MessageEvents.messageOnTopic;


public class Client {

    private static String clientName = "";

    public static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    public Thread thread = new Thread(this::consoleRoutine);

    public void consoleRoutine() {

        System.out.println("Enter a name for the client: ");
        ClientData.setReceivingQueueServerClient();
        ServerMessagesListener serverMessagesListener = new ServerMessagesListener(ClientData.receivingQueueServerClient);
        serverMessagesListener.thread.start();


        try {
            clientName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while reading client's name");
        }

        ClientData.clientId = clientName;

        ClientServerMessageSender.sendConnectionRequest();



        System.out.println("Waiting for server response...");

        while (!ClientData.isConnected.get()) {
            if (ClientData.connectionDupName.get()) {
                ClientData.connectionDupName.set(false);
                System.out.println("Name already in use!");
                System.out.println("Enter another name for the client: ");
                try {
                    clientName = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error while reading client's name");
                }

                ClientData.clientId = clientName;

                ClientServerMessageSender.sendConnectionRequest();
            }
        }

        ClientHeartbeat clientHeartbeat = new ClientHeartbeat();
        clientHeartbeat.thread.start();

        while (!this.thread.isInterrupted()) {
            clientMenu();
        }

    }

    public void clientMenu() {
        showMenu();

        int selectedOption = 0;

        boolean valueOk = true;

        do {

            selectedOption = 0;

            try {
                selectedOption = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Reading error");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Invalid character");
            }

            valueOk = true;

            switch (selectedOption) {
                case 1: {
                    sendMessageToOtherClients();
                }
                break;

                case 2: {
                    readMessagesFromClients();
                }
                break;

                case 3: {
                    readSpecificTopic();
                }
                break;

                case 4: {
                    publishOnTopic();
                }
                break;

                case 5: {
                    createTopic();

                }
                break;

                case 6: {
                    configureTopicTTL();
                }
                break;

                case 7: {
                    requestClientList();
                }
                break;

                case 8: {
                    showConfig();
                }

                default: {
                    System.out.println("Selected option is not available, please choose again: ");
                    showMenu();
                    valueOk = false;
                }
            }

        } while (!valueOk);

    }

    public static void showMenu() {

        System.out.println("This is client '" + clientName + "' and it can do the following: ");

        System.out.println("1) Send a message to other clients.");
        System.out.println("2) Read messages from other clients.");
        System.out.println("3) Read/Subscribe to a specific topic.");
        System.out.println("4) Publish on a topic.");
        System.out.println("5) Create a new topic.");
        System.out.println("6) Configure default topic message TTL.");
        System.out.println("7) Request connected clients list from server.");
        System.out.println("8) Show config.");

        System.out.println("What do you want to do? Enter an option:");

    }

    public void sendMessageToOtherClients() {
        System.out.print("Write client's name: ");

        try {
            String partnerName = reader.readLine();
            if (!partnerName.equals(ClientData.clientId)) {


                Optional<Partner> maybePartner = ClientData.connectedPartners
                        .exportAsList()
                        .stream()
                        .filter((e) -> e.getPartnerId()
                                .equals(partnerName)).findFirst();
                if (maybePartner.isPresent() && maybePartner.get().getSendingQueue().length() > 0) {
                    ServerMessagesListener.sendMessage(maybePartner.get().getSendingQueue(), partnerName);
                } else {
                    ClientServerMessageSender.sendCheckIfPartnerConnected(partnerName);
                    while (!ClientData.waitForResponseBool.get()) ;

                    ClientData.waitForResponseBool.compareAndSet(true, false);
                }
            } else {
                System.out.println("You can not send messages to yourself");
            }

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Reading error");
        }

    }

    public void readMessagesFromClients() {
        System.out.print("Connected clients: ");
        ClientData.connectedPartners.exportAsList().forEach((e) -> System.out.print(e.getPartnerId() + " "));
        System.out.println();
        System.out.print("Write client's name: ");

        try {
            var ref = new Object() {
                String partnerId = reader.readLine();
            };
            while (ClientData.connectedPartners.exportAsList().stream().noneMatch((e) -> e.getPartnerId().equals(ref.partnerId))) {
                System.out.println("Entered name is invalid. Enter a valid client name");
                ref.partnerId = reader.readLine();
            }
            Optional<Partner> partner = ClientData.connectedPartners.exportAsList().stream().filter((e) -> e.getPartnerId().equals(ref.partnerId)).findFirst();

//            partner.get().getPartnersMessagesConsumer().displayMessages();
            if (partner.isPresent()) {
                System.out.println("Incoming messages from " + partner.get().getPartnerId() + ". Write 'EXIT' to return to main menu.");

                DirectMessageDisplay directMessageDisplay = new DirectMessageDisplay(partner.get());
                directMessageDisplay.thread.start();

                String message = reader.readLine();
                while (!message.equals("EXIT")) {
                    message = reader.readLine();
                }
                directMessageDisplay.thread.interrupt();
                while (directMessageDisplay.thread.isAlive()) ;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readSpecificTopic() {
        System.out.println("Write topic name: ");
        String topicName = null;
        try {

            topicName = reader.readLine();

            if (ClientData.topicSubscriptions.exists(topicName)) {
                TopicConsumer tc = ClientData.topicSubscriptions.get(topicName);
                TopicDisplayRoutine tdr = new TopicDisplayRoutine(tc);
                System.out.println("Reading messages from: " + topicName + ". Write 'EXIT' to return to main menu.");
                tdr.thread.start();
                String message = reader.readLine();
                while (!message.equals("EXIT")) {
                    message = reader.readLine();
                }
                tdr.thread.interrupt();
                while (tdr.thread.isAlive()) ;

            } else {
                ClientTopicOperations.subscribeToTopic(topicName);
                System.out.println("Subscribed to [" + topicName + "] succesfully!");
            }

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Reading error");

        }


    }

    public void publishOnTopic() {
        System.out.println("Write topic's name:");
        String topicName = null;
        try {
            topicName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Write message: ");
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleMessage sm = new SimpleMessage();
        sm.setMessage(message);
        sm.setMessageReceiver("");
        sm.setMessageSender(ClientData.clientId);
        ClientTopicOperations.publishToTopic(topicName, sm);


    }

    public void createTopic() {
        System.out.println("Write topic's name:");
        String topicName = null;
        try {
            topicName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message = "";

        SimpleMessage sm = new SimpleMessage();
        sm.setMessage(message);
        sm.setMessageReceiver("");
        sm.setMessageSender(ClientData.clientId);
        if (!ClientData.topicPublishers.exists(topicName)) {
            ClientTopicOperations.publishToTopic(topicName, sm);
        } else {
            System.out.println("Topic [" + topicName + "] exists.");
        }


    }

    public void configureTopicTTL() {
        //To Be Added
    }

    public void requestClientList() {
        SimpleEventMessage message = new SimpleEventMessage();
        message.setMessageReceiver(RabbitMQConstants.serverId);
        message.setMessageSender(clientName);
        message.setMessage("");
        message.setEventType(MessageEvents.requestConnectedClientsList);

        ClientServerMessageSender.sendServerRequest(message);

        while (!ClientData.gotResponse.get()) {
        }

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

    public void showConfig() {
        System.out.println("Config for client:");
        System.out.println("Maximum message queue: " + DefaultConfig.nrMaxOfMessagesAccepted);
        System.out.println("Client's messages TTL: " + DefaultConfig.topicMessageTTL / 1000 + " s");
    }

}
