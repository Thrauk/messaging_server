package messaging_server.client.data;

import messaging_server.client.consumer.PartnersMessagesConsumer;
import messaging_server.client.models.Partner;
import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.TopicConsumer;
import messaging_server.rabbitMQ.TopicProducer;
import messaging_server.structures.SafeMap;
import messaging_server.structures.SafeQueue;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


public class ClientData {
    public static String clientId = "";
    public static String receivingQueueServerClient;
    public static final AtomicBoolean isConnected = new AtomicBoolean(false);
        public static final AtomicBoolean waitForResponseBool = new AtomicBoolean(false);
//    public static ResponseEnum waitForResponse = ResponseEnum.INITIAL;

    public static final SafeQueue<SimpleEventMessage> messagesToSend = new SafeQueue<>();
    public static final SafeQueue<Partner> connectedPartners = new SafeQueue<>();
    //    public static final SafeQueue<PartnersMessagesConsumer> partnersMessagesConsumers = new SafeQueue<>();
    public static final SafeMap<String, TopicProducer> topicPublishers = new SafeMap<>();
    public static final SafeMap<String, TopicConsumer> topicSubscriptions = new SafeMap<>();
    //public static final SafeMap<String, SafeQueue<String>> listOfMessages = new SafeMap<>();

    public static final AtomicBoolean gotResponse = new AtomicBoolean(false);
    public static final AtomicBoolean connectionDupName = new AtomicBoolean( false);

    public static void setReceivingQueueServerClient() {
        ClientData.receivingQueueServerClient = UUID.randomUUID().toString();
    }

    public static void addOrSetPartnerListener(String partnerId, PartnersMessagesConsumer partnersMessagesConsumer) {
        Optional<Partner> partnerOptional = connectedPartners
                .exportAsList()
                .stream()
                .filter(partner -> partner.getPartnerId().equals(partnerId))
                .findFirst();

        if (partnerOptional.isEmpty()) {
            connectedPartners.add(new Partner(partnerId, partnersMessagesConsumer));
        } else {
            partnerOptional.get().setPartnersMessagesConsumer(partnersMessagesConsumer);
        }
    }

    public static void addOrSetPartnerQueue(String partnerId, String queue) {
        Optional<Partner> partnerOptional = connectedPartners
                .exportAsList()
                .stream()
                .filter(partner -> partner.getPartnerId().equals(partnerId))
                .findFirst();

        if (partnerOptional.isEmpty()) {
            connectedPartners.add(new Partner(partnerId, queue));
        } else {
            partnerOptional.get().setSendingQueue(queue);
        }
    }


}
