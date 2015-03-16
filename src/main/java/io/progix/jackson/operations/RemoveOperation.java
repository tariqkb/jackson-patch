package io.progix.jackson.operations;

import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchFailedException;
import io.progix.jackson.JsonPatchFormatException;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchUtil;

public class RemoveOperation {

    public static JsonNode apply(JsonPatchOperation instruction, JsonNode rootNode) throws JsonPatchFailedException {

        try {
            rootNode = JsonPatchUtil.remove(instruction.getPath(), rootNode);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchFailedException(instruction, e);
        }

        return rootNode;
    }
}
