package io.progix.jackson.exceptions;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchOperationType;

public class JsonPatchTestFailedException extends JsonPatchFailedException {

    public JsonPatchTestFailedException(JsonPointer pointer, Object expected, Object actual) throws
            JsonPatchFormatException {
        super("A JSON patch test failed: The value located at '" + pointer.toString() + "' (" + actual + ") " +
                "is not equal to " + expected);
    }

    public JsonPatchTestFailedException(JsonPointer path, JsonNode value, String message) {
        super(JsonPatchOperationType.TEST, path, value, null, message);
    }

    public JsonPatchTestFailedException(JsonPointer path, JsonNode value, Throwable cause) {
        super(JsonPatchOperationType.TEST, path, value, null, cause);
    }

    public JsonPatchTestFailedException(JsonPointer path, JsonNode value, String message, Throwable cause) {
        super(JsonPatchOperationType.TEST, path, value, null, message, cause);
    }
}
