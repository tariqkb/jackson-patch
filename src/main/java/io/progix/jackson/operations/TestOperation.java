package io.progix.jackson.operations;

import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchFailedException;
import io.progix.jackson.JsonPatchFormatException;
import io.progix.jackson.JsonPatchInstruction;
import io.progix.jackson.JsonPatchUtil;

public class TestOperation {

    public static void apply(JsonPatchInstruction instruction, JsonNode root) throws JsonPatchFailedException {

        try {
            if (!JsonPatchUtil.test(instruction.getValue(), instruction.getPath(), root)) {
                throw new JsonPatchFailedException(instruction, "Test failed");
            }
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchFailedException(instruction, e);
        }
    }
}
