package messaging_server.server.routines;

import messaging_server.server.data.ServerData;

public class ServerDisconnectClientRoutine extends ServerRoutine {
    @Override
    protected void routine() {
        String disconnectedClientId = ServerData.disconnectedClients.popExisting();
        //System.out.println("HERE ABOUT " + disconnectedClientId);
        ServerData.notifyDisconnectedPartner(disconnectedClientId);
    }
}
