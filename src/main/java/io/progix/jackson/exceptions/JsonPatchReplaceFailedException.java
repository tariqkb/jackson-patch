package io.progix.jackson.exceptions;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.JsonPatchOperationType;

public class JsonPatchReplaceFailedException extends JsonPatchFailedException {

    public JsonPatchReplaceFailedException(JsonPointer path, JsonNode value, String message) {
        super(JsonPatchOperationType.REPLACE, path, value, null, message);
    }

    public JsonPatchReplaceFailedException(JsonPointer path, JsonNode value, String message, Throwable
            cause) {
        super(JsonPatchOperationType.REPLACE, path, value, null, message, cause);
    }

    public JsonPatchReplaceFailedException(JsonPointer path, JsonNode value, Throwable cause) {
        super(JsonPatchOperationType.REPLACE, path, value, null, cause);
    }
}
