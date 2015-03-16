package io.progix.jackson.exceptions;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchOperationType;

public class JsonPatchAddFailedException extends JsonPatchFailedException {

    public JsonPatchAddFailedException(JsonPointer path, JsonNode value, String message) {
        super(JsonPatchOperationType.ADD, path, value, null, message);
    }

    public JsonPatchAddFailedException(JsonPointer path, JsonNode value,  String message, Throwable cause) {
        super(JsonPatchOperationType.ADD, path, value, null, message, cause);
    }

    public JsonPatchAddFailedException(JsonPointer path, JsonNode value, Throwable cause) {
        super(JsonPatchOperationType.ADD, path, value, null, cause);
    }
}
