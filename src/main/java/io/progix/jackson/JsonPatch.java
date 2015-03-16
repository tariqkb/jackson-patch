package io.progix.jackson;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.progix.jackson.exceptions.JsonPatchAddFailedException;
import io.progix.jackson.exceptions.JsonPatchCopyFailedException;
import io.progix.jackson.exceptions.JsonPatchFailedException;
import io.progix.jackson.exceptions.JsonPatchFormatException;
import io.progix.jackson.exceptions.JsonPatchMoveFailedException;
import io.progix.jackson.exceptions.JsonPatchRemoveFailedException;
import io.progix.jackson.exceptions.JsonPatchReplaceFailedException;
import io.progix.jackson.exceptions.JsonPatchTestFailedException;

/**
 * Helper class that provides methods to apply JSON patches to JSON documents.
 */
public class JsonPatch {

    /**
     * Performs a series of {@link JsonPatchOperation} on a target {@link JsonNode} patch document.
     * <p/>
     * Note that the patch document is not modified.
     *
     * @param patchOperations the {@link JsonPatchOperation}'s to be performed
     * @param rootDocument    the JSON document to be patched
     * @return the patched document
     * @throws JsonPatchFailedException if there were any issues applying the patch
     */
    public static JsonNode apply(JsonPatchOperation[] patchOperations, JsonNode rootDocument) throws
            JsonPatchFailedException {

        for (JsonPatchOperation instruction : patchOperations) {
            switch (instruction.getOperation()) {

                case ADD:
                    rootDocument = add(instruction.getPath(), instruction.getValue(), rootDocument);
                    break;
                case REMOVE:
                    rootDocument = remove(instruction.getPath(), rootDocument);
                    break;
                case REPLACE:
                    rootDocument = replace(instruction.getPath(), instruction.getValue(), rootDocument);
                    break;
                case COPY:
                    rootDocument = copy(instruction.getPath(), instruction.getFrom(), rootDocument);
                    break;
                case TEST:
                    test(instruction.getPath(), instruction.getValue(), rootDocument);
                    break;
                case MOVE:
                    rootDocument = move(instruction.getPath(), instruction.getFrom(), rootDocument);
                    break;
                default:
                    throw new JsonPatchFailedException(instruction,
                            "Operation '" + instruction.getOperation() + "' is not valid.");
            }
        }
        return rootDocument;
    }

    /**
     * Applies an add operation to a JSON document.
     * <p/>
     * Note this method does not modify given JSON document.
     *
     * @param path     {@link JsonPointer} representation of the target path to perform the add operation
     * @param value    {@link JsonNode} representation of value to add
     * @param document the JSON document
     * @return the modified JSON document
     * @throws JsonPatchFailedException if there are any problems patching the document
     */
    public static JsonNode add(JsonPointer path, JsonNode value, JsonNode document) throws JsonPatchFailedException {

        try {
            document = JsonPatchUtil.put(value, path, document);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchAddFailedException(path, value, e);
        }

        return document;
    }

    /**
     * Applies a copy operation to a JSON document.
     * <p/>
     * Note this method does not modify given JSON document.
     *
     * @param path     {@link JsonPointer} representation of target path in JSON document to copy source entity to
     * @param from     {@link JsonPointer} representation of document path for source entity to copy
     * @param document the JSON document
     * @return the modified JSON document
     * @throws JsonPatchFailedException if there are any problems patching the document
     */
    public static JsonNode copy(JsonPointer path, JsonPointer from, JsonNode document) throws
            JsonPatchFailedException {

        try {
            JsonNode valueNode = JsonPatchUtil.at(document, from);
            document = JsonPatchUtil.put(valueNode, path, document);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchCopyFailedException(path, from, e);
        }

        return document;
    }

    /**
     * Applies a move operation to a JSON document.
     * <p/>
     * Note this method does not modify given JSON document.
     *
     * @param path     {@link JsonPointer} representation of target path in JSON document to move source entity to
     * @param from     {@link JsonPointer} representation of document path for source entity to move
     * @param document the JSON document
     * @return the modified JSON document
     * @throws JsonPatchFailedException if there any problems patching the document
     */
    public static JsonNode move(JsonPointer path, JsonPointer from, JsonNode document) throws JsonPatchFailedException {

        try {
            JsonNode valueNode = JsonPatchUtil.at(document, from);
            document = JsonPatchUtil.remove(from, document);
            document = JsonPatchUtil.put(valueNode, path, document);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchMoveFailedException(path, from, e);
        }

        return document;
    }

    /**
     * Applies a remove operation to a JSON document.
     * <p/>
     * Note this method does not modify given JSON document.
     *
     * @param path     {@link JsonPointer} representation of target path in JSON document to remove
     * @param document the JSON document
     * @return the modified JSON document
     * @throws JsonPatchFailedException if there are any problems patching the document
     */
    public static JsonNode remove(JsonPointer path, JsonNode document) throws JsonPatchFailedException {

        try {
            document = JsonPatchUtil.remove(path, document);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchRemoveFailedException(path, e);
        }

        return document;
    }

    /**
     * Applies an replace operation to a JSON document.
     * <p/>
     * Note this method does not modify given JSON document.
     *
     * @param path     {@link JsonPointer} representation the target path to perform the replace operation
     * @param value    {@link JsonNode} representation of value to replace
     * @param document the JSON document
     * @return the modified JSON document
     * @throws JsonPatchFailedException if there are any problems patching the document
     */
    public static JsonNode replace(JsonPointer path, JsonNode value, JsonNode document) throws
            JsonPatchFailedException {

        try {
            document = JsonPatchUtil.remove(path, document);
            document = JsonPatchUtil.put(value, path, document);
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchReplaceFailedException(path, value, e);
        }

        return document;
    }

    /**
     * Performs a test operation to a JSON document.
     * <p/>
     * Note this method does not modify given JSON document.
     *
     * @param path     {@link JsonPointer} representation of the target path to perform the test operation
     * @param value    {@link JsonNode} representation of value to test
     * @param document the JSON document
     * @throws JsonPatchFailedException if there are any problems patching the document or if the equality test fails.
     */
    public static void test(JsonPointer path, JsonNode value, JsonNode document) throws JsonPatchFailedException {

        try {
            if (!JsonPatchUtil.test(value, path, document)) {
                JsonNode actual = JsonPatchUtil.at(document, path);
                throw new JsonPatchTestFailedException(path, value, actual);
            }
        } catch (JsonPatchFormatException e) {
            throw new JsonPatchTestFailedException(path, value, e);
        }
    }

}