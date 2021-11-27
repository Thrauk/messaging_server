package messaging_server.models;


import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonModel {
    public static void main(String [] args) {
        try {
            File file = new File("test.json");
            FileWriter fileWriter = new FileWriter(file, true);

            ObjectMapper mapper = new ObjectMapper();

            SequenceWriter seqWriter = mapper.writer().writeValuesAsArray(fileWriter);
            //seqWriter.write(new SimpleMessage("Andrei","Mihai" ,"I have apples."));
            seqWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
