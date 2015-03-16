package io.progix.jackson.exceptions;

import com.fasterxml.jackson.core.JsonPointer;
import io.progix.jackson.JsonPatchOperationType;

public class JsonPatchMoveFailedException extends JsonPatchFailedException {

    public JsonPatchMoveFailedException(JsonPointer path, JsonPointer from, String message) {
        super(JsonPatchOperationType.MOVE, path, null, from, message);
    }

    public JsonPatchMoveFailedException(JsonPointer path, JsonPointer from, String message, Throwable
            cause) {
        super(JsonPatchOperationType.MOVE, path, null, from, message, cause);
    }

    public JsonPatchMoveFailedException(JsonPointer path, JsonPointer
            from, Throwable cause) {
        super(JsonPatchOperationType.MOVE, path, null, from, cause);
    }
}
