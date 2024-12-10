# Deduplicate Leads Application

This application processes a JSON file containing lead records, removes duplicates based on specified rules, and generates a deduplicated JSON file. It also logs the changes made during the deduplication process.

## Features

* Deduplicates lead records based on:
    * Duplicate IDs (`_id` field).
    * Duplicate emails (`email` field).
* Preference is given to:
    * Records with the newest `entryDate`.
    * If dates are identical, the record appearing last in the input.
* Generates a deduplicated JSON file as output.
* Logs changes in a log file, detailing:
    * Source record.
    * Final record.
    * Field-level changes.

## Application Architecture

### High-Level Overview

* **InputParser:**
    * Reads the input JSON file.
    * Validates its structure.
    * Returns a list of lead records.
* **DeduplicationProcessor:**
    * Removes duplicates based on the rules.
    * Keeps track of duplicates using `_id` and `email`.
    * Logs changes during the deduplication process.
* **OutputWriter:**
    * Writes the deduplicated records to a JSON file.
* **Log4j:**
    * Handles logging of application messages and errors.

## Project Structure
```
deduplicate-leads/
├── pom.xml                     # Maven configuration file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com.example.deduplication/
│   │   │   │   ├── DeduplicateLeads.java         # Main entry point
│   │   │   │   ├── InputParser.java              # Reads and parses input JSON
│   │   │   │   ├── DeduplicationProcessor.java   # Handles deduplication logic
│   │   │   │   ├── OutputWriter.java             # Writes the output JSON file
│   ├── resources/
│   │   │   ├── log4j2.xml                        # Log4j configuration
│   ├── test/
│   │   ├── java/
│   │   │   ├── com.example.deduplication/
│   │   │   │   ├── InputParserTest.java          # Unit tests for InputParser
│   │   │   │   ├── DeduplicationProcessorTest.java  # Unit tests for DeduplicationProcessor
│   │   │   │   ├── OutputWriterTest.java         # Unit tests for OutputWriter
│   │   ├── resources/
│   │   │   ├── test-input.json                   # Test data for unit tests
│   │   │   ├── test-invalid-input.json           # Invalid test data
│   │   │   ├── deduplication-log.txt             # Generated log file

```
## Build Instructions

### Prerequisites

* Java 17 or higher
* Maven 3.6 or higher

### Steps

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd deduplicate-leads 
   ```


2. Build the application:

   ```bash
   mvn clean package
   ```

3. The built JAR file will be available in the target/ directory:

   ```bash
    target/deduplicate-leads-app.jar
      ```
## Run the Application
1. Ensure you have an input JSON file (e.g., leads.json) in the project directory.

2. Run the application using the following command:

 ```bash
   java -jar target/deduplicate-leads-app.jar  src/main/resources/code_challenge_leads.json src/main/resources/output.json
   ```

3. After running:

- Output JSON: A deduplicated version of the input JSON.
- Log File: A file (deduplication_log.txt) logging all changes during deduplication.


## Testing
1. To run the tests:

 ```bash
   mvn test
   ```

2. The test suite validates:

- Input file parsing (InputParser).
- Deduplication logic and change logging (DeduplicationProcessor).
- Output JSON generation (OutputWriter).

## Logging
- The application uses Log4j for logging.
- Logs are written to the console by default.
- Example log format:
```
  2024-12-09 12:00:00 [main] INFO  DeduplicationProcessor - Starting deduplication process.
  2024-12-09 12:00:01 [main] INFO  DeduplicationProcessor - Deduplication process completed. 5 records remaining.
```

## Sample Input and Output
1. Input (leads.json):
```
{
  "leads": [
    { "_id": "1", "email": "a@example.com", "entryDate": "2022-01-01T12:00:00Z" },
    { "_id": "1", "email": "b@example.com", "entryDate": "2022-01-02T12:00:00Z" }
  ]
}
```
2. Output (output.json):

```
{
  "leads": [
    { "_id": "1", "email": "b@example.com", "entryDate": "2022-01-02T12:00:00Z" }
  ]
}

```

3. Log (deduplication_log.txt):
```
   Duplicate detected by ID:
   Old Record: {_id=1, email=a@example.com, entryDate=2022-01-01T12:00:00Z}
   New Record: {_id=1, email=b@example.com, entryDate=2022-01-02T12:00:00Z}
   Field 'email' changed: 'a@example.com' -> 'b@example.com'
   Field 'entryDate' changed: '2022-01-01T12:00:00Z' -> '2022-01-02T12:00:00Z'
```
