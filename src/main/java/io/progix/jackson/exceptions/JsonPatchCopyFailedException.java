package io.progix.jackson.exceptions;

import com.fasterxml.jackson.core.JsonPointer;
import io.progix.jackson.JsonPatchOperationType;

public class JsonPatchCopyFailedException extends JsonPatchFailedException {

    public JsonPatchCopyFailedException(JsonPointer path, JsonPointer from, String message) {
        super(JsonPatchOperationType.COPY, path, null, from, message);
    }

    public JsonPatchCopyFailedException(JsonPointer path, JsonPointer from, String message, Throwable
            cause) {
        super(JsonPatchOperationType.COPY, path, null, from, message, cause);
    }

    public JsonPatchCopyFailedException(JsonPointer path, JsonPointer from, Throwable cause) {
        super(JsonPatchOperationType.COPY, path, null, from, cause);
    }
}
