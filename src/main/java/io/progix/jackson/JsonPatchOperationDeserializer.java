package io.progix.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;

import java.io.IOException;

public class JsonPatchOperationDeserializer extends JsonDeserializer<JsonPatchOperation> {

    private UntypedObjectDeserializer untypedObjectDeserializer;

    public JsonPatchOperationDeserializer() {
        untypedObjectDeserializer = new UntypedObjectDeserializer();
    }

    @Override
    public JsonPatchOperation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        untypedObjectDeserializer.resolve(ctxt);

        JsonNode node = jp.getCodec().readTree(jp);

        JsonNode operationNode = node.get("op");
        if (operationNode == null || !operationNode.isTextual()) {
            throw new JsonPatchFormatException("'op' is missing or not a string: " + operationNode);
        }

        JsonPatchOperationType operation = JsonPatchOperationType
                .valueOf(operationNode.asText().toUpperCase());

        JsonNode pathNode = node.get("path");
        if (pathNode == null || !pathNode.isTextual()) {
            throw new JsonPatchFormatException("'path' is missing or not a string: " + pathNode);
        }

        if (operation == JsonPatchOperationType.REMOVE) {
            return new JsonPatchOperation(operation, JsonPointer.compile(pathNode.asText()));
        } else if (operation == JsonPatchOperationType.COPY || operation == JsonPatchOperationType.MOVE) {
            JsonNode fromNode = node.get("from");
            if (fromNode == null || !fromNode.isTextual()) {
                throw new JsonPatchFormatException(
                        "'from' is missing or not a string (" + fromNode + ") and is required " +
                                "for operation '" + operation + "'");
            }
            return new JsonPatchOperation(operation, JsonPointer.compile(pathNode.asText()),
                    JsonPointer.compile(fromNode.asText()));
        } else if (operation == JsonPatchOperationType.ADD || operation == JsonPatchOperationType.REPLACE || operation == JsonPatchOperationType.TEST) {
            JsonNode valueNode = node.get("value");
            if (valueNode == null || valueNode.isMissingNode()) {
                throw new JsonPatchFormatException(
                        "'value' is missing and is required for operation '" + operation + "'");
            }

            return new JsonPatchOperation(operation, JsonPointer.compile(pathNode.asText()), valueNode);
        }

        throw new RuntimeException("Unknown operation.");
    }
}