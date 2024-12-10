package com.example.deduplication;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OutputWriterTest {

    @Test
    void writeOutputFile_shouldWriteValidJson() throws IOException {
        OutputWriter writer = new OutputWriter();
        List<Map<String, Object>> deduplicatedLeads = new ArrayList<>(List.of(
                createLead("1", "a@example.com", "2022-01-01T12:00:00Z"),
                createLead("2", "b@example.com", "2022-01-02T12:00:00Z")
        ));

        File outputFile = new File("src/test/resources/output.json");
        writer.writeOutputFile(outputFile.getAbsolutePath(), deduplicatedLeads);

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    private Map<String, Object> createLead(String id, String email, String entryDate) {
        Map<String, Object> lead = new HashMap<>();
        lead.put("_id", id);
        lead.put("email", email);
        lead.put("entryDate", entryDate);
        return lead;
    }
}
