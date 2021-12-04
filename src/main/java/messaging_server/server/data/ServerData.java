package messaging_server.server.data;

import messaging_server.client.Client;
import messaging_server.models.SimpleEventMessage;
import messaging_server.models.SimpleMessage;
import messaging_server.server.Server;
import messaging_server.server.models.ClientModel;
import messaging_server.server.models.MessageToSend;
import messaging_server.server.models.json.DisconnectedPartner;
import messaging_server.structures.SafeMap;
import messaging_server.structures.SafeQueue;

import java.util.List;
import java.util.stream.Collectors;

public class ServerData {
    public static final SafeQueue<MessageToSend> messagesToSend = new SafeQueue<>();
    public static final SafeMap<String, ClientModel> connectedClients = new SafeMap<>();

    public static final SafeQueue<SimpleEventMessage> incomingMessages = new SafeQueue<>();
    public static final SafeQueue<SimpleEventMessage> incomingConnectionRequests = new SafeQueue<>();

    public static final SafeQueue<String> disconnectedClients = new SafeQueue<>();

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

    public static void notifyDisconnectedPartner(String disconnectedId) {
        List<ClientModel> activePartners = connectedClients
                .exportValuesAsList()
                .stream()
                .filter((clientModel -> clientModel.hasPartner(disconnectedId)))
                .collect(Collectors.toList());

        for(ClientModel clientModel : activePartners) {
            ServerData.messagesToSend.add(new MessageToSend(new DisconnectedPartner(disconnectedId, clientModel.getClientId())));
        }

    }


}
