package io.progix.jackson.operations;

import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchFailedException;
import io.progix.jackson.JsonPatchFormatException;
import io.progix.jackson.JsonPatchInstruction;
import io.progix.jackson.JsonPatchUtil;

import java.io.IOException;

public class RemoveOperation {

    public static JsonNode apply(JsonPatchInstruction instruction, JsonNode rootNode) throws IOException {

        try {
            rootNode = JsonPatchUtil.remove(instruction.getPath(), rootNode);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchFailedException(instruction, e);
        }

        return rootNode;
    }
}
