package io.progix.jackson.operations;

import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchFailedException;
import io.progix.jackson.JsonPatchFormatException;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchUtil;

public class ReplaceOperation {

    public static JsonNode apply(JsonPatchOperation instruction, JsonNode root) throws JsonPatchFailedException {

        try {
            root = JsonPatchUtil.remove(instruction.getPath(), root);
            root = JsonPatchUtil.put(instruction.getValue(), instruction.getPath(), root);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchFailedException(instruction, e);
        }

        return root;
    }
}
