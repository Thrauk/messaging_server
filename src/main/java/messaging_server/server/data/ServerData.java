package messaging_server.server.data;

import messaging_server.models.SimpleMessage;

public class ServerData {
    public static final SafeQueue<SimpleMessage> messagesToSend = new SafeQueue<>();
    public static final SafeQueue<String> connectedClients = new SafeQueue<>();

}
