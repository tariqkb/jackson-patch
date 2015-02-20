package io.progix.jackson;

public class PatchFormatException extends Exception {

    public PatchFormatException(JsonPatchInstruction instruction, String message) {
        super(formatInstruction(instruction) + " failed: " + message);
    }

    public PatchFormatException(JsonPatchInstruction instruction, String message, Throwable cause) {
        super(formatInstruction(instruction) + " failed: " + message, cause);
    }

    public PatchFormatException(JsonPatchInstruction instruction, Throwable cause) {
        super(formatInstruction(instruction) + " failed: ", cause);
    }

    private static String formatInstruction(JsonPatchInstruction instruction) {
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
