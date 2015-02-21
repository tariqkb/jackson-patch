package io.progix.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = JsonPatchInstructionDeserializer.class)
public class JsonPatchInstruction {

    public static enum JsonPatchOperationType {
        ADD, REMOVE, REPLACE, COPY, TEST, MOVE;

        @JsonCreator
        public static JsonPatchOperationType forValue(String value) {
            return JsonPatchOperationType.valueOf(value.toUpperCase());
        }
    }

    private JsonPatchOperationType operation;
    private JsonPointer path;
    private JsonPointer from;
    private JsonNode value;

    public JsonPatchInstruction(JsonPatchOperationType operation, JsonPointer path, JsonPointer from) {
        this.operation = operation;
        this.path = path;
        this.from = from;
        this.value = null;
    }

    public JsonPatchInstruction(JsonPatchOperationType operation, JsonPointer path) {
        this.operation = operation;
        this.path = path;
    }

    public JsonPatchInstruction(JsonPatchOperationType operation, JsonPointer path, JsonNode value) {
        this.operation = operation;
        this.path = path;
        this.value = value;
    }

    public void setOperation(JsonPatchOperationType operation) {
        this.operation = operation;
    }

    public void setPath(JsonPointer path) {
        this.path = path;
    }

    public void setFrom(JsonPointer from) {
        this.from = from;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }

    public JsonPatchOperationType getOperation() {
        return operation;
    }

    public JsonPointer getPath() {
        return path;
    }

    public JsonPointer getFrom() {
        return from;
    }

    public JsonNode getValue() {
        return value;
    }
}
