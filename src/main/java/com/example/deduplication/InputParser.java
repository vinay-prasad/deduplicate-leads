package com.example.deduplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InputParser {
    private static final Logger logger = LogManager.getLogger(InputParser.class);
    private final ObjectMapper objectMapper;

    public InputParser() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Map<String, Object>> parseInputFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("Input file does not exist: {}", filePath);
            throw new IOException("Input file does not exist: " + filePath);
        }

        logger.info("Reading input file: {}", filePath);

        Map<String, Object> rootNode = objectMapper.readValue(file, Map.class);
        if (!rootNode.containsKey("leads")) {
            logger.error("Input JSON must contain a 'leads' key.");
            throw new IOException("Input JSON must contain a 'leads' key.");
        }

        logger.info("Successfully parsed input file.");
        return (List<Map<String, Object>>) rootNode.get("leads");
    }
}
