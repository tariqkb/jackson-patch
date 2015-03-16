package io.progix.jackson.exceptions;

import com.fasterxml.jackson.core.JsonPointer;
import io.progix.jackson.JsonPatchOperationType;

public class JsonPatchRemoveFailedException extends JsonPatchFailedException {

    public JsonPatchRemoveFailedException(JsonPointer path, String message) {
        super(JsonPatchOperationType.REMOVE, path, null, null, message);
    }

    public JsonPatchRemoveFailedException(JsonPointer path, String message, Throwable cause) {
        super(JsonPatchOperationType.REMOVE, path, null, null, message, cause);
    }

    public JsonPatchRemoveFailedException(JsonPointer path, Throwable cause) {
        super(JsonPatchOperationType.REMOVE, path, null, null, cause);
    }
}
