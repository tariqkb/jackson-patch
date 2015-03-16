package io.progix.jackson;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.progix.jackson.exceptions.JsonPatchFormatException;

public class JsonPatchUtil {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static JsonNode remove(JsonPointer pathPointer, JsonNode rootNode) throws JsonPatchFormatException {
        rootNode = rootNode.deepCopy();

        JsonPointer lastPointer = pathPointer.last();
        JsonPointer parentPointer = pathPointer.head();

        if (parentPointer == null) {
            //  Assumes target node references whole document
            ((ObjectNode) rootNode).removeAll();
            return rootNode;
        }

        JsonNode parentTargetNode = rootNode.at(parentPointer);

        if (parentTargetNode.isArray()) {
            ArrayNode arrayTargetNode = (ArrayNode) parentTargetNode;

            if (lastPointer.mayMatchElement()) {
                //  If insertion target is an index, simply insert
                int index = lastPointer.getMatchingIndex();

                if (index >= arrayTargetNode.size() || index < 0) {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "an invalid index:" +
                            " Must be a positive number and not be greater than the size of the array (" +
                            arrayTargetNode.size() + ").");
                }

                arrayTargetNode.remove(index);
            } else if (lastPointer.mayMatchProperty()) {
                if (lastPointer.getMatchingProperty().equals("-")) {
                    if (arrayTargetNode.size() == 0) {
                        throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                                "an invalid index:" +
                                " Cannot access last element of empty array.");
                    }

                    arrayTargetNode.remove(arrayTargetNode.size() - 1);
                } else {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "an invalid index:" +
                            " Must be either a positive integer or '-'");
                }
            } else {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid index:" +
                        " Must be either a positive integer or '-'");
            }
        } else if (parentTargetNode.isObject()) {
            ObjectNode objectParentTargetNode = (ObjectNode) parentTargetNode;

            if (lastPointer.mayMatchProperty() && !lastPointer.getMatchingProperty().trim().equals("-")) {
                JsonNode removed = objectParentTargetNode.remove(lastPointer.getMatchingProperty());
                if (removed == null) {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "a non-existent member.");
                }
            } else {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid property:" +
                        " Must not be an integer or the special character '-' ");
            }
        } else {
            throw new JsonPatchFormatException(
                    "The path '" + pathPointer.toString() + "' tries to access a member of a value node.");
        }

        return rootNode;
    }

    public static JsonNode put(JsonNode valueNode, JsonPointer pathPointer,
            JsonNode rootNode) throws JsonPatchFormatException {
        rootNode = rootNode.deepCopy();

        JsonPointer lastPointer = pathPointer.last();
        JsonPointer parentPointer = pathPointer.head();

        JsonNode parentTargetNode;
        if (parentPointer == null) {
            //  Replace whole document with value provided
            return valueNode;
        }

        parentTargetNode = rootNode.at(parentPointer);

        //  Case 1: Added element to an array
        if (parentTargetNode.isArray()) {
            ArrayNode arrayTargetNode = (ArrayNode) parentTargetNode;

            if (lastPointer.mayMatchElement()) {
                //  If insertion target is an index, simply insert
                int index = lastPointer.getMatchingIndex();

                if (index > arrayTargetNode.size()) {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "an invalid index:" +
                            " Must not be greater than the size of the array (" + arrayTargetNode.size() + ").");
                }

                arrayTargetNode.insert(index, valueNode);
            } else if (lastPointer.mayMatchProperty()) {
                //  If insertion target is '-', insert into end of list
                if (lastPointer.getMatchingProperty().equals("-")) {
                    arrayTargetNode.insert(arrayTargetNode.size(), valueNode);
                } else {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "an invalid index:" +
                            " Must be either a positive integer or '-'");
                }
            } else {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid index:" +
                        " Must be either a positive integer or '-'");
            }
        } else if (parentTargetNode.isObject()) {
            //  Case 2: Add/replace an object member.

            ObjectNode objectParentTargetNode = (ObjectNode) parentTargetNode;

            if (lastPointer.mayMatchProperty() && !lastPointer.getMatchingProperty().trim().equals("-")) {
                objectParentTargetNode.set(lastPointer.getMatchingProperty(), valueNode);
            } else {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid property:" +
                        " Must not be an integer or the special character '-' ");
            }

        } else {
            if (parentTargetNode.isMissingNode()) {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid object member." +
                        " Parent member '" + parentPointer.toString() + "' must exist");
            } else {
                throw new JsonPatchFormatException("The parent member '" + parentTargetNode.asText() + "' " +
                        "located at '" + parentPointer + "' is not an object or " +
                        "an array and cannot be added to.");
            }
        }

        return rootNode;
    }

    public static boolean test(JsonNode valueNode, JsonPointer pathPointer,
            JsonNode rootNode) throws JsonPatchFormatException {

        JsonPointer lastPointer = pathPointer.last();
        JsonPointer parentPointer = pathPointer.head();

        JsonNode parentTargetNode;
        if (parentPointer == null) {
            parentTargetNode = rootNode;
            parentPointer = JsonPointer.compile("/");
        } else {
            parentTargetNode = rootNode.at(parentPointer);
        }

        if (parentTargetNode.isArray()) {
            ArrayNode arrayTargetNode = (ArrayNode) parentTargetNode;

            if (lastPointer.mayMatchProperty() && !lastPointer.mayMatchElement()) {
                if (lastPointer.getMatchingProperty().equals("-")) {
                    pathPointer = parentPointer
                            .append(JsonPointer.compile("/" + String.valueOf(arrayTargetNode.size() - 1)));
                } else {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "an invalid index:" +
                            " Must be either a positive integer or '-'");
                }
            }
        }

        JsonNode targetNode = rootNode.at(pathPointer);

        if (targetNode.isMissingNode()) {
            throw new JsonPatchFormatException(
                    "The path '" + pathPointer.toString() + "' does not point to an existing value.");
        }

        if (valueNode.getNodeType().equals(targetNode.getNodeType())) {
            if (valueNode.isValueNode()) {
                if (valueNode.isFloatingPointNumber()) {
                    return valueNode.decimalValue().compareTo(targetNode.decimalValue()) == 0;
                } else {
                    return valueNode.equals(targetNode);
                }
            } else if (valueNode.isContainerNode()) {
                return valueNode.equals(targetNode);
            }
        }
        return false;
    }

    public static JsonNode at(JsonNode rootNode, JsonPointer pathPointer) throws JsonPatchFormatException {

        JsonPointer lastPointer = pathPointer.last();
        JsonPointer parentPointer = pathPointer.head();

        JsonNode parentTargetNode;
        if (parentPointer == null) {
            return rootNode;
        }

        parentTargetNode = rootNode.at(parentPointer);

        if (parentTargetNode.isArray()) {
            ArrayNode arrayTargetNode = (ArrayNode) parentTargetNode;

            if (lastPointer.mayMatchElement()) {
                //  If insertion target is an index, simply insert
                int index = lastPointer.getMatchingIndex();

                if (index > arrayTargetNode.size()) {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "an invalid index:" +
                            " Must not be greater than the size of the array (" + arrayTargetNode.size() + ").");
                }
                return arrayTargetNode.get(index);
            } else if (lastPointer.mayMatchProperty()) {
                //  If insertion target is '-', insert into end of list
                if (!lastPointer.getMatchingProperty().equals("-")) {
                    throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to " +
                            "an invalid index:" +
                            " Must be either a positive integer or '-'");
                }

                return arrayTargetNode.get(arrayTargetNode.size() - 1);
            } else {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid index:" +
                        " Must be either a positive integer or '-'");
            }
        } else if (parentTargetNode.isObject()) {
            //  Case 2: Add/replace an object member.

            ObjectNode objectParentTargetNode = (ObjectNode) parentTargetNode;

            if (lastPointer.mayMatchProperty() && !lastPointer.getMatchingProperty().trim().equals("-")) {
                return objectParentTargetNode.get(lastPointer.getMatchingProperty());
            } else {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid property:" +
                        " Must not be an integer or the special character '-' ");
            }

        } else {
            if (parentTargetNode.isMissingNode()) {
                throw new JsonPatchFormatException("The path '" + pathPointer.toString() + "' points to an " +
                        "invalid object member." +
                        " Parent member '" + parentPointer.toString() + "' must exist");
            } else {
                throw new JsonPatchFormatException("The parent member '" + parentTargetNode.asText() + "' " +
                        "located at '" + parentPointer + "' is not an object or " +
                        "an array and cannot be added to.");
            }
        }
    }


}
