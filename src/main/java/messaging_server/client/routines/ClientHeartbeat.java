package messaging_server.client.routines;

import messaging_server.client.config.DefaultConfig;
import messaging_server.client.data.ClientData;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.JsonMessageProducer;
import messaging_server.rabbitMQ.RabbitMQConstants;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientHeartbeat {

    public ClientHeartbeat() {
        message.setMessageSender(ClientData.clientId);
        message.setMessageReceiver("server");
        message.setMessage("heartbeat");
    }

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final JsonMessageProducer jsonMessageProducer = new JsonMessageProducer();
    private final SimpleEventMessage message = new SimpleEventMessage();

    public void start() {
        executorService.scheduleAtFixedRate(this::sendHeartbeat, 0, DefaultConfig.heartbeatIntervalMS, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executorService.shutdown();
    }

    private void sendHeartbeat() {
        jsonMessageProducer.sendEventMessageOnQueue(message, RabbitMQConstants.serverReceivingQueue);
    }


}
