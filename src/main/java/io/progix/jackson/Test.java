package io.progix.jackson;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree("{" +
                "\"test\": {" +
                "\"a\": []" +
                "}" +
                "}");

        JsonPointer pointer = JsonPointer.compile("/test/a");
        JsonNode node = root.at(pointer);

        if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
        }

        JsonNode test = mapper.readTree("\"-\"");

        String fg = "22";
    }
}
