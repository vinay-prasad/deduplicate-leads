package com.example.deduplication;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeduplicationProcessorTest {

    @Test
    void deduplicate_shouldRemoveDuplicatesByIdOrEmail() throws IOException {
        DeduplicationProcessor processor = new DeduplicationProcessor();
        List<Map<String, Object>> leads = new ArrayList<>(List.of(
                createLead("1", "a@example.com", "2022-01-01T12:00:00Z"),
                createLead("2", "b@example.com", "2022-01-02T12:00:00Z"),
                createLead("1", "c@example.com", "2022-01-03T12:00:00Z"), // Duplicate ID
                createLead("3", "b@example.com", "2022-01-04T12:00:00Z")  // Duplicate email
        ));

        File logFile = new File("src/test/resources/deduplication-log.txt");
        List<Map<String, Object>> deduplicated = processor.deduplicate(leads, logFile.getAbsolutePath());

        assertEquals(2, deduplicated.size());
        assertTrue(deduplicated.stream().anyMatch(lead -> lead.get("email").equals("c@example.com")));
        assertTrue(deduplicated.stream().anyMatch(lead -> lead.get("email").equals("b@example.com")));
    }

    @Test
    void deduplicate_shouldLogChanges() throws IOException {
        DeduplicationProcessor processor = new DeduplicationProcessor();
        List<Map<String, Object>> leads = new ArrayList<>(List.of(
                createLead("1", "a@example.com", "2022-01-01T12:00:00Z"),
                createLead("1", "b@example.com", "2022-01-03T12:00:00Z") // Duplicate ID
        ));

        File logFile = new File("src/test/resources/deduplication-log.txt");
        processor.deduplicate(leads, logFile.getAbsolutePath());

        assertTrue(logFile.exists());
        assertTrue(logFile.length() > 0);
    }

    private Map<String, Object> createLead(String id, String email, String entryDate) {
        Map<String, Object> lead = new HashMap<>();
        lead.put("_id", id);
        lead.put("email", email);
        lead.put("entryDate", entryDate);
        return lead;
    }
}
