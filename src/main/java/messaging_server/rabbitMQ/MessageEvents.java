package messaging_server.rabbitMQ;

public class MessageEvents {
    public static String connectionRequest = "connection-request";
    public static String heartbeat = "heartbeat";
    public static String checkIfConnected = "check-if-connected";
    public static String checkIfConnectedResponseFailed = "check-if-connected-response-failed";
    public static String checkIfConnectedResponseSuccessful = "check-if-connected-response-successful";
    public static String connectionRequestResponseSuccessful = "connection-request-response-successful";
    public static String connectionRequestResponseFailed = "connection-request-response-failed";
    public static String listenForNewMessages = "listen-for-new-messages";
}