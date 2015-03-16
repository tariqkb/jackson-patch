package io.progix.jackson.operations;

import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchFailedException;
import io.progix.jackson.JsonPatchFormatException;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchUtil;

public class AddOperation {

    public static JsonNode apply(JsonPatchOperation instruction, JsonNode rootNode) throws JsonPatchFailedException {

        JsonNode valueNode = instruction.getValue();

        try {
            rootNode = JsonPatchUtil.put(valueNode, instruction.getPath(), rootNode);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchFailedException(instruction, e);
        }

        return rootNode;
    }

}
