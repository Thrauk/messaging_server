package messaging_server.rabbitMQ;

public class MessageEvents {
    public static String connectionRequest = "connection-request";
    public static String heartbeat = "heartbeat";
    public static String checkIfConnected = "check-if-connected";
    public static String connectionRequestResponseSuccessful = "connection-request-response-successful";
    public static String connectionRequestResponseFailed = "connection-request-response-failed";
}
