package messaging_server.server.routines;

public abstract class ServerRoutine {
    public Thread thread = new Thread(this::doRoutine);
    protected abstract void routine();
    protected void doRoutine() {
        while (!thread.isInterrupted()) {
            routine();
        }
    }
}
