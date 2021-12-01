package messaging_server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static byte[] getBytes(JsonObject jsonObject)
    {
        try {
            return objectMapper.writeValueAsBytes(jsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("No bytes for you!");
        }
        return null;
    }

    public static String stringify(JsonObject jsonObject) {
        try {
            return objectMapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Error while converting to String!";
    }
}
