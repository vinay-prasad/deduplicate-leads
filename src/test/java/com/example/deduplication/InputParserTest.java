package com.example.deduplication;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    @Test
    void parseInputFile_validFile_shouldReturnLeads() throws IOException {
        InputParser parser = new InputParser();
        File file = new File("src/test/resources/test-input.json");

        List<Map<String, Object>> leads = parser.parseInputFile(file.getAbsolutePath());

        assertNotNull(leads);
        assertEquals(3, leads.size());
    }

    @Test
    void parseInputFile_missingFile_shouldThrowException() {
        InputParser parser = new InputParser();
        String invalidFilePath = "invalid-path.json";

        Exception exception = assertThrows(IOException.class, () -> parser.parseInputFile(invalidFilePath));

        assertTrue(exception.getMessage().contains("Input file does not exist"));
    }

    @Test
    void parseInputFile_missingLeadsKey_shouldThrowException() {
        InputParser parser = new InputParser();
        File file = new File("src/test/resources/test-invalid-input.json");

        Exception exception = assertThrows(IOException.class, () -> parser.parseInputFile(file.getAbsolutePath()));

        assertTrue(exception.getMessage().contains("Input JSON must contain a 'leads' key"));
    }
}
