package io.progix.jackson;

import java.io.IOException;

public class JsonPatchFormatException extends IOException {

    public JsonPatchFormatException(String message) {
        super(message);
    }

    public JsonPatchFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPatchFormatException(Throwable cause) {
        super(cause);
    }
}
