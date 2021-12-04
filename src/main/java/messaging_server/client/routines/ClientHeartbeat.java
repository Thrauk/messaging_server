package messaging_server.client.routines;

import messaging_server.client.config.DefaultConfig;
import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.JsonMessageProducer;
import messaging_server.rabbitMQ.MessageEvents;
import messaging_server.rabbitMQ.RabbitMQConstants;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientHeartbeat extends ClientRoutine {

    public ClientHeartbeat() {
        message.setMessageSender(ClientData.clientId);
        message.setMessageReceiver(RabbitMQConstants.serverId);
        message.setEventType(MessageEvents.heartbeat);
        message.setMessage("");
    }

    private final JsonMessageProducer jsonMessageProducer = new JsonMessageProducer();
    private final SimpleEventMessage message = new SimpleEventMessage();


    @Override
    protected void routine() {
        //System.out.println("Sending heartbeat");
        jsonMessageProducer.sendEventMessageOnQueue(message, RabbitMQConstants.serverReceivingQueue);
        try {
            Thread.sleep(DefaultConfig.heartbeatIntervalMS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
