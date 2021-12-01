package messaging_server.client.data;

import messaging_server.client.models.Partner;
import messaging_server.models.SimpleEventMessage;
import messaging_server.models.SimpleMessage;
import messaging_server.structures.SafeQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientData {
    public static String clientId = "";
    public static String receivingQueueServerClient;
    public static final AtomicBoolean isConnected = new AtomicBoolean(false);
    public static final SafeQueue<SimpleEventMessage> messagesToSend = new SafeQueue<>();

    public static void setReceivingQueueServerClient() {
        ClientData.receivingQueueServerClient = clientId + "-" + UUID.randomUUID();
    }
}
