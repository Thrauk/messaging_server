package messaging_server.server.routines;

import messaging_server.rabbitMQ.RabbitMQConstants;
import messaging_server.server.consumer.ServerMainConsumer;
import messaging_server.server.producer.ServerMessageProducer;

public class ServerListenForMessages extends ServerRoutine{
    @Override
    protected void routine() {
        ServerMainConsumer serverMainConsumer = new ServerMainConsumer(RabbitMQConstants.serverReceivingQueue);
        ServerMessageProducer serverMessageSender = new ServerMessageProducer();

        serverMainConsumer.thread.start();
        serverMessageSender.thread.start();

        try {
            serverMainConsumer.thread.join();
            serverMessageSender.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
