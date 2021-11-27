package messaging_server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObject {
    protected ObjectMapper objectMapper;
    public JsonObject(){
        objectMapper =new ObjectMapper();
    }
  public byte[] getBytes()
  {
      try {
          return objectMapper.writeValueAsBytes(this);
      } catch (JsonProcessingException e) {
          e.printStackTrace();
          System.out.println("No bytes for you!");
      }
  }
}
