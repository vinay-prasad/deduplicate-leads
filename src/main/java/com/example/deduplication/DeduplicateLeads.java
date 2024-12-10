package com.example.deduplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DeduplicateLeads {
    private static final Logger logger = LogManager.getLogger(DeduplicateLeads.class);

    public static void main(String[] args) {
        if (args.length < 2) {
            logger.error("Usage: java -jar deduplicate-leads-app.jar <input_file> <output_file>");
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = args[1];
        String logFilePath = "deduplication_log.txt";

        try {
            InputParser parser = new InputParser();
            List<Map<String, Object>> leads = parser.parseInputFile(inputFilePath);

            DeduplicationProcessor processor = new DeduplicationProcessor();
            List<Map<String, Object>> deduplicatedLeads = processor.deduplicate(leads, logFilePath);

            OutputWriter writer = new OutputWriter();
            writer.writeOutputFile(outputFilePath, deduplicatedLeads);

            logger.info("Deduplication complete.");
            logger.info("Output written to: {}", outputFilePath);
            logger.info("Log written to: {}", logFilePath);

        } catch (IOException e) {
            logger.error("Error processing files: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        }
    }
}
