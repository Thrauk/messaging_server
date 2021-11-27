package messaging_server.client.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.Consumer;
import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerMessagesListener extends Consumer {
    public ServerMessagesListener(String queueName, ClientData clientData) {
        super(queueName);
        this.setThreadRunnable(this::listenServerMainQueueTesting);
        this.clientData = clientData;
    }

    private final ClientData clientData;

    public void listenServerMainQueueTesting(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                String m = new String(message.getBody(), StandardCharsets.UTF_8);

                SimpleMessage rec = objectMapper.readValue(m, SimpleMessage.class);
                if(rec != null) {
                    clientData.isConnected = true;
                    System.out.println("Client connected succ");
                }
            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();
        }

    }
}
