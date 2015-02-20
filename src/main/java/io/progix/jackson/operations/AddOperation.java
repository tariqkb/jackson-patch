package io.progix.jackson.operations;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.progix.jackson.JsonPatchInstruction;
import io.progix.jackson.PatchFormatException;

import java.io.IOException;

public class AddOperation {

    public static String apply(JsonPatchInstruction instruction, String document) throws IOException,
            PatchFormatException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(document);

        //  Pointer information
        JsonPointer pointer = JsonPointer.compile(instruction.getPath());
        JsonPointer lastPointer = pointer.last();
        JsonPointer parentPointer = pointer.head();

        //  Nodes in document
        JsonNode targetNode = root.at(pointer);
        JsonNode parentTargetNode = root.at(parentPointer);

        //  Value node in instruction
        JsonNode valueNode = mapper.readTree(instruction.getValue().trim());

        //  Case 1: Added element to an array
        if (parentTargetNode.isArray()) {
            ArrayNode arrayTargetNode = (ArrayNode) targetNode;

            if (lastPointer.mayMatchElement()) {
                //  If insertion target is an index, simply insert
                int index = lastPointer.getMatchingIndex();

                if (index > arrayTargetNode.size()) {
                    throw new PatchFormatException(instruction, "The path '" + instruction.getPath() + "' points to " +
                            "an invalid index:" +
                            " Must not be greater than the size of the array (" + arrayTargetNode.size() + ").");
                }

                arrayTargetNode.insert(index, valueNode);
            } else if (lastPointer.mayMatchProperty()) {
                //  If insertion target is '-', insert into end of list
                if (lastPointer.getMatchingProperty().equals("-")) {
                    arrayTargetNode.insert(arrayTargetNode.size() - 1, valueNode);
                } else {
                    throw new PatchFormatException(instruction, "The path '" + instruction.getPath() + "' points to " +
                            "an invalid index:" +
                            " Must be either a positive integer or '-'");
                }
            }

            throw new PatchFormatException(instruction, "The path '" + instruction.getPath() + "' points to an " +
                    "invalid index:" +
                    " Must be either a positive integer or '-'");
        } else if (parentTargetNode.isObject()) {
            //  Case 2: Add/replace an object member.

            ObjectNode objectParentTargetNode = (ObjectNode) parentTargetNode;

            if (lastPointer.mayMatchProperty() && !lastPointer.getMatchingProperty().trim().equals("-")) {
                objectParentTargetNode.set(lastPointer.getMatchingProperty(), valueNode);
            } else {
                throw new PatchFormatException(instruction, "The path '" + instruction.getPath() + "' points to an " +
                        "invalid property:" +
                        " Must not be an integer or the special character '-' ");
            }

        } else {
            if (parentTargetNode.isMissingNode()) {
                throw new PatchFormatException(instruction, "The path '" + instruction.getPath() + "' points to an " +
                        "invalid object member." +
                        " Parent member '" + parentPointer.toString() + "' must exist");
            } else {
                throw new PatchFormatException(instruction, "The parent member '" + parentTargetNode.asText() + "' " +
                        "located at '" + parentPointer + "' is not an object or " +
                        "an array and cannot be added to.");
            }
        }

        return "";
    }

}
