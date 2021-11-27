package messaging_server.server.routines;

public abstract class ServerRoutine {
    public Thread thread = new Thread(this::routine);
    protected abstract void routine();

}
