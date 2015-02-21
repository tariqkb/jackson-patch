package io.progix.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.progix.jackson.operations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatchProcessor {

    public static JsonNode apply(ObjectMapper mapper, JsonNode patchDocument,
            JsonNode rootDocument) throws IOException, JsonPatchFailedException {

        List<JsonPatchInstruction> jsonPatchInstructions = new ArrayList<>();

        try {
            Collections.addAll(jsonPatchInstructions, mapper.convertValue(patchDocument, JsonPatchInstruction[].class));
        } catch (IllegalArgumentException e) {
            throw new JsonPatchFailedException("Patch document could not be constructed.", e);
        }

        //  Identify each instruction and apply each to target document
        for (JsonPatchInstruction instruction : jsonPatchInstructions) {
            switch (instruction.getOperation()) {

                case ADD:
                    rootDocument = AddOperation.apply(instruction, rootDocument);
                    break;
                case REMOVE:
                    rootDocument = RemoveOperation.apply(instruction, rootDocument);
                    break;
                case REPLACE:
                    rootDocument = ReplaceOperation.apply(instruction, rootDocument);
                    break;
                case COPY:
                    rootDocument = CopyOperation.apply(instruction, rootDocument);
                    break;
                case TEST:
                    TestOperation.apply(instruction, rootDocument);
                    break;
                case MOVE:
                    rootDocument = MoveOperation.apply(instruction, rootDocument);
                    break;
                default:
                    throw new JsonPatchFailedException(instruction,
                            "Operation '" + instruction.getOperation() + "' is not valid.");
            }
        }
        return rootDocument;
    }
}