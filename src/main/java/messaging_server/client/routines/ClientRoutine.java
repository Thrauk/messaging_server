package messaging_server.client.routines;

public abstract class ClientRoutine {
    public Thread thread = new Thread(this::doRoutine);
    protected abstract void routine();
    protected void doRoutine() {
        while (!thread.isInterrupted()) {
            routine();
        }
    }
}
