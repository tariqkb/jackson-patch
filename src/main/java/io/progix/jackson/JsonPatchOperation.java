package io.progix.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = JsonPatchOperationDeserializer.class)
@JsonSerialize(using = JsonPatchOperationSerializer.class)
public class JsonPatchOperation {

    private JsonPatchOperationType operation;
    private JsonPointer path;
    private JsonPointer from;
    private JsonNode value;

    public JsonPatchOperation(JsonPatchOperationType operation, JsonPointer path, JsonPointer from) {
        this.operation = operation;
        this.path = path;
        this.from = from;
        this.value = null;
    }

    public JsonPatchOperation(JsonPatchOperationType operation, JsonPointer path) {
        this.operation = operation;
        this.path = path;
    }

    public JsonPatchOperation(JsonPatchOperationType operation, JsonPointer path, JsonNode value) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        JsonPatchOperation that = (JsonPatchOperation) o;

        if (from != null ? !from.equals(that.from) : that.from != null)
            return false;
        if (operation != that.operation)
            return false;
        if (path != null ? !path.equals(that.path) : that.path != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operation != null ? operation.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
