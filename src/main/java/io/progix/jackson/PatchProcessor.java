package io.progix.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.progix.jackson.operations.AddOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatchProcessor {

    public static String apply(String patchDocument, String targetDocument) throws IOException, PatchFormatException {
        //  Convert patch document to set of JsonObject instructions
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootPatchNode = mapper.readTree(patchDocument);

        List<JsonPatchInstruction> jsonPatchInstructions = new ArrayList<>();
        Collections.addAll(jsonPatchInstructions, mapper.convertValue(rootPatchNode,
                JsonPatchInstruction[].class));

        //  Identify each instruction and apply each to target document
        for (JsonPatchInstruction instruction : jsonPatchInstructions) {
            switch (instruction.getOperation()) {

                case ADD:
                    targetDocument = AddOperation.apply(instruction, targetDocument);
                    break;
                case REMOVE:
                    break;
                case REPLACE:
                    break;
                case COPY:
                    break;
                case TEST:
                    break;
                case MOVE:
                    break;
            }
        }
        return targetDocument;
    }
}
