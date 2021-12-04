package messaging_server.server.routines;

import messaging_server.models.SimpleEventMessage;
import messaging_server.rabbitMQ.JsonMessageProducer;
import messaging_server.rabbitMQ.RabbitMQConstants;
import messaging_server.server.Server;
import messaging_server.server.config.DefaultConfig;
import messaging_server.server.data.ServerData;
import messaging_server.server.models.ClientModel;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerHeartbeatCheckRoutine extends ServerRoutine {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ServerDisconnectClientRoutine serverDisconnectClientRoutine = new ServerDisconnectClientRoutine();



    public void start() {
        executorService.scheduleAtFixedRate(this::checkHeartbeat, 0, DefaultConfig.heartbeatInterval, TimeUnit.MILLISECONDS);
        serverDisconnectClientRoutine.thread.start();
    }

    public void stop() {
        executorService.shutdown();
        serverDisconnectClientRoutine.thread.interrupt();
    }

    private void checkHeartbeat() {

    }

    @Override
    protected void routine() {
        //System.out.println("Heartbeat check");
        List<ClientModel> connectedClientsList = ServerData.connectedClients.exportValuesAsList();
        for(ClientModel clientModel : connectedClientsList) {
            if(!clientModel.checkAlive()) {
                System.out.println("Client " + clientModel.getClientId() + " failed to respond. Disconnecting.");
                ServerData.disconnectedClients.add(clientModel.getClientId());
                ServerData.connectedClients.removeElementByKey(clientModel.getClientId());
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRoutine(){
        serverDisconnectClientRoutine.thread.start();
        while(!thread.isInterrupted()) {
            routine();
        }
        serverDisconnectClientRoutine.thread.interrupt();
    }

}
