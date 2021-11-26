package messaging_server.server.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.Consumer;
import messaging_server.models.SimpleMessage;
import messaging_server.server.Server;
import messaging_server.server.data.ServerData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerMainConsumer extends Consumer {

    public ServerMainConsumer(String queueName, ServerData serverData) {
        super(queueName);
        this.serverData = serverData;
        this.setThreadRunnable(this::listenServerMainQueueTesting);
    }

    private final ServerData serverData;

    public void listenServerMainQueue() {

        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                String m = new String(message.getBody(), StandardCharsets.UTF_8);
                synchronized (serverData.connectedClients) {
                    serverData.connectedClients.add(m);
                }

            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();

        }

    }

    public void listenServerMainQueueTesting(){
        ObjectMapper objectMapper = new ObjectMapper();
        //SimpleMessage sm = new SimpleMessage("client-1", "server", "ConnectionReq");
        //byte[] sm_bytes = objectMapper.writeValueAsBytes(sm);

        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                SimpleMessage rec = objectMapper.readValue(message.getBody(), SimpleMessage.class);
                if(rec != null) {
                    synchronized (serverData.messagesToSend) {
                        serverData.messagesToSend.add(new SimpleMessage("server", rec.getMessageSender(), "Conn succ"));
                    }
                    synchronized (serverData.connectedClients) {
                        serverData.connectedClients.add(rec.getMessageSender());
                    }
                }


            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();

        }

    }

}
