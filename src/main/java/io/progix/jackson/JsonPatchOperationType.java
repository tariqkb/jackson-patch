package io.progix.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum JsonPatchOperationType {

    ADD, REMOVE, REPLACE, COPY, TEST, MOVE;

    @JsonCreator
    public static io.progix.jackson.JsonPatchOperationType forValue(String value) {
        return io.progix.jackson.JsonPatchOperationType.valueOf(value.toUpperCase());
    }
}
