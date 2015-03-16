package io.progix.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class JsonPatchOperationSerializer extends JsonSerializer<JsonPatchOperation> {

    @Override
    public void serialize(JsonPatchOperation value, JsonGenerator gen,
            SerializerProvider serializers) throws IOException, JsonProcessingException {

        gen.writeStartObject();

        gen.writeStringField("op", value.getOperation().toString().toLowerCase());
        gen.writeStringField("path", value.getPath().toString());

        if (value.getFrom() != null) {
            gen.writeStringField("from", value.getFrom().toString());
        }

        if (value.getValue() != null) {
            gen.writeObjectField("value", value.getValue());
        }

        gen.writeEndObject();

    }
}
