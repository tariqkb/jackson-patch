package io.progix.jackson.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.exceptions.JsonPatchFailedException;
import io.progix.jackson.JsonTestCase;
import io.progix.jackson.JsonPatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Parameterized.class)
public class SpecTests {

    private final JsonTestCase testCase;
    private final ObjectMapper mapper = new ObjectMapper();

    public SpecTests(JsonTestCase testCase) {
        this.testCase = testCase;
    }

    @Test
    public void testCase() throws IOException {
        if (testCase.isDisabled()) {
            return;
        }

        try {
            JsonPatchOperation[] patchOperations = mapper.convertValue(testCase.getPatch(), JsonPatchOperation[].class);
            JsonNode documentNode = mapper.readTree(testCase.getDoc());

            JsonNode resultNode = JsonPatch.apply(patchOperations, documentNode);
            if (testCase.isErrorCase()) {
                fail("Expected error, but there was none: " + testCase.getError());
            }

            //If not, just a test to make sure no exceptions
            if (testCase.getExpected() != null) {
                JsonNode expectedNode = mapper.readTree(testCase.getExpected());

                assertThat(resultNode).isEqualTo(expectedNode);
            }

        } catch (JsonPatchFailedException | IllegalArgumentException e) {
            if (!testCase.isErrorCase()) {
                fail("Did not expect error but got one", e);
            }
        }
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        List<Object[]> datas = new ArrayList<Object[]>();

        ObjectMapper mapper = new ObjectMapper();
        JsonTestCase[] testCases;

        try {
            testCases = mapper
                    .readValue(SpecTests.class.getClassLoader().getResource("tests.json"), JsonTestCase[].class);

            for (JsonTestCase testCase : testCases) {
                datas.add(new Object[]{testCase});
            }

            return datas;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
