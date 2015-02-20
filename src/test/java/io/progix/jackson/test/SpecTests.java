package io.progix.jackson.test;

import org.junit.runners.Parameterized;

import java.util.Collection;

public class SpecTests {
    private final String patchDocument;
    private final String targetDocument;
    private final String expectedDocument;
    private final String comment;
    private final boolean disabled;

    public SpecTests(String patchDocument, String targetDocument, String expectedDocument, String comment, boolean
            disabled) {
        this.patchDocument = patchDocument;
        this.targetDocument = targetDocument;
        this.expectedDocument = expectedDocument;
        this.comment = comment;
        this.disabled = disabled;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        
    }
}
