package io.progix.jackson.operations;

import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchFailedException;
import io.progix.jackson.JsonPatchFormatException;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchUtil;

public class CopyOperation {

    public static JsonNode apply(JsonPatchOperation instruction, JsonNode rootNode) throws JsonPatchFailedException {

        try {
            JsonNode valueNode = JsonPatchUtil.at(rootNode, instruction.getFrom());
            rootNode = JsonPatchUtil.put(valueNode, instruction.getPath(), rootNode);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchFailedException(instruction, e);
        }

        return rootNode;
    }
}
