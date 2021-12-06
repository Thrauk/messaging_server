package messaging_server.client.routines;

import messaging_server.client.models.Partner;

public class DirectMessageDisplay extends ClientRoutine {
    private Partner partner;

    public DirectMessageDisplay(Partner partner) {
        this.partner = partner;
    }

    @Override
    protected void routine() {
        partner.getPartnersMessagesConsumer().displayMessages();
    }
}
