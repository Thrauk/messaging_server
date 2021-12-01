package messaging_server.server.data;

import messaging_server.client.Client;
import messaging_server.models.SimpleEventMessage;
import messaging_server.models.SimpleMessage;
import messaging_server.server.models.ClientModel;
import messaging_server.server.models.MessageToSend;
import messaging_server.structures.SafeMap;
import messaging_server.structures.SafeQueue;

public class ServerData {
    public static final SafeQueue<MessageToSend> messagesToSend = new SafeQueue<>();
    public static final SafeMap<String, ClientModel> connectedClients = new SafeMap<>();

    public static final SafeQueue<SimpleEventMessage> incomingMessages = new SafeQueue<>();
    public static final SafeQueue<SimpleEventMessage> incomingConnectionRequests = new SafeQueue<>();

    public static String getConnectedClientQueue(String clientId) {
        ClientModel client = connectedClients.get(clientId);
        if(client != null) {
            return client.getReceivingQueue();
        } else {
            return null;
        }
    }

    public static void addClient(String clientId, String clientQueue) {
        connectedClients.add(clientId, new ClientModel(clientId,clientQueue));
    }


}
