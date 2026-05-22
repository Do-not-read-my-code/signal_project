package com.data_management;

import com.alerts.AlertGenerator;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages storage and retrieval of patient data.
 */
public class DataStorage {

    private static DataStorage instance;
    private Map<Integer, Patient> patientMap;

    /**
     * Creates a data storage object.
     */
    public DataStorage() {
        this.patientMap = new ConcurrentHashMap<>();
    }

    /**
     * Gets the shared storage instance.
     *
     * @return the singleton storage instance
     */
    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    /**
     * Adds one patient record to storage.
     *
     * @param patientId the patient ID
     * @param measurementValue the measurement value
     * @param recordType the record type
     * @param timestamp the timestamp
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.computeIfAbsent(patientId, Patient::new);
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Gets patient records from a time range.
     *
     * @param patientId the patient ID
     * @param startTime the start timestamp
     * @param endTime the end timestamp
     * @return matching patient records
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>();
    }

    /**
     * Gets all patients.
     *
     * @return all stored patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    /**
     * Simple demonstration main method.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DataStorage storage = DataStorage.getInstance();
        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);

        for (PatientRecord record : records) {
            System.out.println("Record for Patient ID: " + record.getPatientId() + ", Type: " + record.getRecordType() + ", Data: " + record.getMeasurementValue() + ", Timestamp: " + record.getTimestamp());
        }

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }
}
