package io.progix.jackson.exceptions;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchOperationType;

public class JsonPatchFailedException extends RuntimeException {

    public JsonPatchFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPatchFailedException(String message) {
        super(message);
    }

    public JsonPatchFailedException(JsonPatchOperation instruction, String message) {
        super(formatInstruction(instruction.getOperation(), instruction.getPath(), instruction.getValue(),
                instruction.getFrom()) + " failed: " + message);
    }

    public JsonPatchFailedException(JsonPatchOperation instruction, String message, Throwable cause) {
        super(formatInstruction(instruction.getOperation(), instruction.getPath(), instruction.getValue(),
                instruction.getFrom()) + " failed: " + message, cause);
    }

    public JsonPatchFailedException(JsonPatchOperation instruction, Throwable cause) {
        super(formatInstruction(instruction.getOperation(), instruction.getPath(), instruction.getValue(),
                instruction.getFrom()) + " failed: ", cause);
    }

    public JsonPatchFailedException(JsonPatchOperationType type, JsonPointer path, JsonNode value, JsonPointer from,
                                    String message) {
        super(formatInstruction(type, path, value, from) + " failed: " + message);
    }

    public JsonPatchFailedException(JsonPatchOperationType type, JsonPointer path, JsonNode value, JsonPointer from,
                                    String message, Throwable cause) {
        super(formatInstruction(type, path, value, from) + " failed: " + message, cause);
    }

    public JsonPatchFailedException(JsonPatchOperationType type, JsonPointer path, JsonNode value, JsonPointer from,
                                    Throwable cause) {
        super(formatInstruction(type, path, value, from) + " failed: ", cause);
    }

    private static String formatInstruction(JsonPatchOperationType type, JsonPointer path, JsonNode value,
                                            JsonPointer from) {
        String formatted = type + " ";
        switch (type) {

            case ADD:
                formatted += value + " to " + path;
                break;
            case REMOVE:
                formatted += path;
                break;
            case REPLACE:
                formatted += path + " with " + value;
                break;
            case COPY:
                formatted += path + " to " + from;
                break;
            case TEST:
                formatted += path + " is " + value;
                break;
            case MOVE:
                formatted += path + " to " + from;
                break;
        }
        return formatted;
    }
}
