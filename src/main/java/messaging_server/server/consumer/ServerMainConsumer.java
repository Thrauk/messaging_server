package messaging_server.server.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import messaging_server.rabbitMQ.Consumer;
import messaging_server.models.SimpleMessage;
import messaging_server.server.data.ServerData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerMainConsumer extends Consumer {


    public ServerMainConsumer(String queueName) {
        super(queueName);
    }
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void listener() {
        try {
            channel.basicConsume(this.queueName, true, (consumerTag, message) -> {
                System.out.println("Got message");
                String m = new String(message.getBody(), StandardCharsets.UTF_8);
                System.out.println(m);
                //System.out.println(message.getBody());

                SimpleMessage rec = objectMapper.readValue(m, SimpleMessage.class);
                System.out.println("Sender is " + rec.getMessageSender());


                SimpleMessage sm = new SimpleMessage();
                sm.setMessageSender("server");
                sm.setMessageReceiver(rec.getMessageSender());
                sm.setMessage("Conn succ");
                ServerData.messagesToSend.add(sm);


                ServerData.connectedClients.add(rec.getMessageSender());


            }, consumerTag -> {});
        } catch (IOException e) {
            System.out.println("Error listening " + queueName);
            e.printStackTrace();

        }
    }


    public void listenServerMainQueueTesting(){

        //SimpleMessage sm = new SimpleMessage("client-1", "server", "ConnectionReq");
        //byte[] sm_bytes = objectMapper.writeValueAsBytes(sm);



    }

}
