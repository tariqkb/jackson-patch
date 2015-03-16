package io.progix.jackson;

public class JsonPatchFailedException extends RuntimeException {

    public JsonPatchFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPatchFailedException(String message) {
        super(message);
    }

    public JsonPatchFailedException(JsonPatchOperation instruction, String message) {
        super(formatInstruction(instruction) + " failed: " + message);
    }

    public JsonPatchFailedException(JsonPatchOperation instruction, String message, Throwable cause) {
        super(formatInstruction(instruction) + " failed: " + message, cause);
    }

    public JsonPatchFailedException(JsonPatchOperation instruction, Throwable cause) {
        super(formatInstruction(instruction) + " failed: ", cause);
    }

    private static String formatInstruction(JsonPatchOperation instruction) {
        String formatted = instruction.getOperation() + " ";
        switch (instruction.getOperation()) {

            case ADD:
                formatted += instruction.getValue() + " to " + instruction.getPath();
                break;
            case REMOVE:
                formatted += instruction.getPath();
                break;
            case REPLACE:
                formatted += instruction.getPath() + " with " + instruction.getValue();
                break;
            case COPY:
                formatted += instruction.getPath() + " to " + instruction.getFrom();
                break;
            case TEST:
                formatted += instruction.getPath() + " is " + instruction.getValue();
                break;
            case MOVE:
                formatted += instruction.getPath() + " to " + instruction.getFrom();
                break;
        }
        return formatted;
    }
}
