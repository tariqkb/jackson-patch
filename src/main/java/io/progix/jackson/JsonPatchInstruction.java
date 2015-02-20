package io.progix.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonPatchInstruction {

    public static enum JsonPatchOperationType {
        ADD, REMOVE, REPLACE, COPY, TEST, MOVE
    }

    @JsonProperty("op")
    private JsonPatchOperationType operation;
    private String path;
    private String from;
    private String value;

    public JsonPatchInstruction(JsonPatchOperationType operation, String path, String from, String value) {
        this.operation = operation;
        this.path = path;
        this.from = from;
        this.value = value;
    }

    public JsonPatchOperationType getOperation() {
        return operation;
    }

    public String getPath() {
        return path;
    }

    public String getFrom() {
        return from;
    }

    public String getValue() {
        return value;
    }
}
