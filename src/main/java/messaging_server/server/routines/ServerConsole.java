package messaging_server.server.routines;

import messaging_server.server.Server;
import messaging_server.server.config.DefaultConfig;
import messaging_server.server.data.ServerData;
import messaging_server.server.models.ClientModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ServerConsole extends ServerRoutine{

    public static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    @Override
    protected void routine() {

        System.out.println("Server started!");

        while(!this.thread.isInterrupted()) {
            showMenu();
            getMenuChoice();
            System.out.println("Command executed successfully");
        }


    }
    @Override
    protected void doRoutine() {
        routine();
    }

    private void showMenu()
    {
        System.out.println("Server menu:");

        System.out.println("1)Show connected clients");
        System.out.println("2)Configure a client");
        System.out.println("3)Show active topics");

        System.out.println("Enter your choice:");
    }

    private void getMenuChoice() {

        boolean valueOk = true;
        int selectedOption = 0;
        do {
            try
            {
                selectedOption = Integer.parseInt(reader.readLine());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Reading error occurred!");
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
                System.out.println("Input is not a number!");
            }

            valueOk = true;

            switch (selectedOption) {
                case 1:
                    showClientsList();
                    break;

                case 2:
                    configureClient();
                    break;

                case 3:
                    showActiveTopics();
                    break;



                default:
                {
                    System.out.println("Selected option is not available, please try again.");
                    showMenu();
                    valueOk = false;
                }
            }
        }while(!valueOk);
    }
    private void showClientsList()
    {
        ArrayList<String> clientsConnected =
           ServerData.connectedClients.exportKeysAsList();

        System.out.println("Currently connected clients:");

        for(String s : clientsConnected)
        {
            System.out.println(s);
        }

        System.out.println("\nPress Enter to return to menu.");

        try
        {
            reader.readLine();
        }
        catch (IOException e)
        {
            System.out.println("Reading error");
        }
    }

    private void configureClient()
    {
        ArrayList<String> clientsConnected =
                ServerData.connectedClients.exportKeysAsList();

        System.out.println("Select client id to configure, or press Enter to return:");

        for(String s : clientsConnected)
        {
            System.out.println(s);
        }

        String selectedClientId = "";

        try
        {
            selectedClientId = reader.readLine();
        }
        catch (IOException e)
        {
            System.out.println("Reading error");
        }

        ClientModel selectedClient =  ServerData.connectedClients.get(selectedClientId);

        if(selectedClient != null)
        {

            clientConfigurationMenu(selectedClient);

            boolean valueOk = true;
            int selectedOption = 0;
            do {
                valueOk = true;
                try
                {
                    selectedOption = Integer.parseInt(reader.readLine());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    System.out.println("Reading error occurred!");
                    valueOk = false;
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                    System.out.println("Input is not a number!");
                    valueOk = false;
                }


                switch (selectedOption) {

                    case 0:
                        break;

                    case 1:
                        clientConfigureTimeout(selectedClient);
                        break;

                    case 2:
                        clientConfigureQueue(selectedClient);
                        break;


                    default:
                    {
                        System.out.println("Selected option is not available, please try again.");
                        clientConfigurationMenu(selectedClient);
                        valueOk = false;
                    }
                }
            }while(!valueOk);


        }
        else
        {
            if(!selectedClientId.equals(""))
                System.out.println("Requested client is not connected!");
        }
    }

    private void showActiveTopics()
    {
        ArrayList<String> listOfTopics = ServerData.topicList.exportAsList();
        for(String iter :listOfTopics)
        {
            System.out.println(iter);
        }
        ArrayList<String> listOfTopicsConnected = ServerData.topicSubscribers.exportKeysAsList();
        System.out.println("");
        for(String iter :listOfTopicsConnected)
        {
            System.out.println(iter);
        }

    }


    private void clientConfigurationMenu(ClientModel client)
    {
        System.out.println("Configuring options for client "+client.getClientId()+":");

        System.out.println("1)Modify client timeout. (Current timeout settings:"+client.getTimeout()+" s)");
        System.out.println("2)Modify client maximum message queue (Current queue settings: maximum "+client.getMaximumMessageQueue()+" messages in queue)");

        System.out.println("\n0)Exit to menu");
        System.out.println("Select option:");

    }

    private void clientConfigureTimeout(ClientModel client)
    {
        System.out.println("Enter new timeout (seconds) for the client:");

        int newTimeout = DefaultConfig.clientTimeout;

        try
        {
            newTimeout = Integer.parseInt(reader.readLine());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Reading error occurred!");
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            System.out.println("Input is not a number!");
        }

        client.setTimeout(newTimeout);
    }

    private void clientConfigureQueue(ClientModel client)
    {
        System.out.println("Enter new maximum messages in queue for the client:");

        int newMaxQueue = DefaultConfig.clientTimeout;

        try
        {
            newMaxQueue = Integer.parseInt(reader.readLine());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Reading error occurred!");
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            System.out.println("Input is not a number!");
        }

        client.setMaximumMessageQueue(newMaxQueue);
    }
}
