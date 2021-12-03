package messaging_server.server.routines;

import messaging_server.server.Server;
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
        System.out.println("4)Configure a topic");

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

                case 4:
                    configureTopic();
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

        System.out.println("Select client id to configure:");

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
            //tbd
        }
    }

    private void showActiveTopics()
    {

    }

    private void configureTopic()
    {

    }

}
