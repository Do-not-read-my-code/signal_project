package com.data_management;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads patient data from simulator output text files.
 */
public class FileDataReader implements DataReader {

    private final String directoryPath;

    /**
     * Creates a file data reader.
     *
     * @param directoryPath the output directory path
     */
    public FileDataReader(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        readFile(dataStorage, "ECG.txt", "ECG");
        readFile(dataStorage, "Saturation.txt", "Saturation");
        readFile(dataStorage, "SystolicPressure.txt", "SystolicPressure");
        readFile(dataStorage, "DiastolicPressure.txt", "DiastolicPressure");
        readFile(dataStorage, "WhiteBloodCells.txt", "WhiteBloodCells");
        readFile(dataStorage, "RedBloodCells.txt", "RedBloodCells");
        readFile(dataStorage, "Cholesterol.txt", "Cholesterol");
        readFile(dataStorage, "Alert.txt", "Alert");
    }

    private void readFile(DataStorage dataStorage, String fileName, String recordType) throws IOException {
        Path filePath = Path.of(directoryPath, fileName);
        if (!Files.exists(filePath)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(dataStorage, line, recordType);
            }
        }
    }

    private void parseLine(DataStorage dataStorage, String line, String recordType) {
        try {
            int patientId = extractPatientId(line);
            long timestamp = extractTimestamp(line);
            double value = extractMeasurementValue(line);
            dataStorage.addPatientData(patientId, value, recordType, timestamp);
        } catch (Exception e) {
            System.err.println("Skipping invalid data line: " + line);
        }
    }

    private int extractPatientId(String line) {
        String patientPart = line.split(",")[0];
        return Integer.parseInt(patientPart.replace("Patient ID:", "").trim());
    }

    private long extractTimestamp(String line) {
        String timestampPart = line.split(",")[1];
        return Long.parseLong(timestampPart.replace("Timestamp:", "").trim());
    }

    private double extractMeasurementValue(String line) {
        String dataPart = line.substring(line.indexOf("Data:") + 5).replace("%", "").trim();
        if (dataPart.equalsIgnoreCase("true")) {
            return 1.0;
        }
        if (dataPart.equalsIgnoreCase("false")) {
            return 0.0;
        }
        return Double.parseDouble(dataPart);
    }
}
