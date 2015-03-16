package io.progix.jackson;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonTestCase {

    @JsonRawValue
    private JsonNode patch;
    @JsonRawValue
    private Object doc;
    @JsonRawValue
    private Object expected;

    private String error;
    private String comment;
    private boolean disabled;

    public JsonTestCase() {
    }

    public JsonNode getPatch() {
        return patch;
    }

    public String getDoc() {
        return doc == null ? null : doc.toString();
    }

    public String getExpected() {
        return expected == null ? null : expected.toString();
    }

    public String getError() {
        return error;
    }

    public String getComment() {
        return comment;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isErrorCase() {
        return error != null;
    }

    public void setPatch(JsonNode patch) {
        this.patch = patch;
    }

    public void setDoc(JsonNode doc) {
        this.doc = doc;
    }

    public void setExpected(JsonNode expected) {
        this.expected = expected;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return comment;
    }
}
