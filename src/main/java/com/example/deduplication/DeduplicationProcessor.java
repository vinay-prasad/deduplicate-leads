package com.example.deduplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

public class DeduplicationProcessor {
    private static final Logger logger = LogManager.getLogger(DeduplicationProcessor.class);

    public List<Map<String, Object>> deduplicate(List<Map<String, Object>> leads, String logFilePath) throws IOException {
        logger.info("Starting deduplication process.");
        Map<String, Map<String, Object>> idMap = new HashMap<>();
        Map<String, Map<String, Object>> emailMap = new HashMap<>();
        List<Map<String, Object>> deduplicatedList = new ArrayList<>();

        try (FileWriter logWriter = new FileWriter(logFilePath)) {
            for (Map<String, Object> lead : leads) {
                String id = (String) lead.get("_id");
                String email = (String) lead.get("email");
                ZonedDateTime entryDate = ZonedDateTime.parse((String) lead.get("entryDate"));

                Map<String, Object> existingRecord = null;
                String duplicateKey = null;

                if (idMap.containsKey(id)) {
                    existingRecord = idMap.get(id);
                    duplicateKey = "ID";
                } else if (emailMap.containsKey(email)) {
                    existingRecord = emailMap.get(email);
                    duplicateKey = "Email";
                }

                if (existingRecord != null) {
                    ZonedDateTime existingDate = ZonedDateTime.parse((String) existingRecord.get("entryDate"));
                    if (entryDate.isAfter(existingDate) || (entryDate.isEqual(existingDate) && leads.indexOf(lead) > leads.indexOf(existingRecord))) {
                        logChanges(logWriter, existingRecord, lead, duplicateKey);
                        deduplicatedList.remove(existingRecord);
                        deduplicatedList.add(lead);
                        idMap.put(id, lead);
                        emailMap.put(email, lead);
                    }
                } else {
                    deduplicatedList.add(lead);
                    idMap.put(id, lead);
                    emailMap.put(email, lead);
                }
            }
        }

        logger.info("Deduplication process completed. {} records remaining.", deduplicatedList.size());
        return deduplicatedList;
    }

    private void logChanges(FileWriter logWriter, Map<String, Object> oldRecord,
                            Map<String, Object> newRecord, String duplicateKey) throws IOException {
        logger.info("Logging changes for duplicate record detected by {}.", duplicateKey);

        logWriter.write("Duplicate detected by " + duplicateKey + ":\n");
        logWriter.write("Old Record: " + oldRecord + "\n");
        logWriter.write("New Record: " + newRecord + "\n");

        for (String key : oldRecord.keySet()) {
            if (!Objects.equals(oldRecord.get(key), newRecord.get(key))) {
                logWriter.write(String.format("Field '%s' changed: '%s' -> '%s'\n",
                        key, oldRecord.get(key), newRecord.get(key)));
            }
        }
        logWriter.write("\n");

        logger.info("Changes logged for duplicate.");
    }
}
