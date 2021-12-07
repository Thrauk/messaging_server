package messaging_server.client.routines;

import messaging_server.client.Client;
import messaging_server.rabbitMQ.TopicConsumer;

public class TopicDisplayRoutine extends ClientRoutine {

    private TopicConsumer tc;

    public TopicDisplayRoutine(TopicConsumer tc) {
        this.tc = tc;
    }

    @Override
    protected void routine() {
        tc.getMessagesFromTopic();
    }
}
