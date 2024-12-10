package com.example.deduplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputWriter {
    private static final Logger logger = LogManager.getLogger(OutputWriter.class);
    private final ObjectMapper objectMapper;

    public OutputWriter() {
        this.objectMapper = new ObjectMapper();
    }

    public void writeOutputFile(String filePath, List<Map<String, Object>> deduplicatedLeads) throws IOException {
        logger.info("Writing output to file: {}", filePath);

        Map<String, Object> outputMap = new HashMap<>();
        outputMap.put("leads", deduplicatedLeads);

        File outputFile = new File(filePath);
        objectMapper.writeValue(outputFile, outputMap);

        logger.info("Output successfully written to file.");
    }
}
