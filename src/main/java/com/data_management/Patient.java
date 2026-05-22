package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 */
public class Patient {

    private int patientId;
    private List<PatientRecord> patientRecords;

    /**
     * Creates a patient with an empty record list.
     *
     * @param patientId the unique patient ID
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds one medical record to this patient.
     *
     * @param measurementValue the measurement value
     * @param recordType the record type
     * @param timestamp the record timestamp
     */
    public synchronized void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    /**
     * Gets records in the selected time interval.
     *
     * @param startTime the start timestamp
     * @param endTime the end timestamp
     * @return records between the two timestamps
     */
    public synchronized List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> filteredRecords = new ArrayList<>();

        for (PatientRecord record : patientRecords) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }

    /**
     * Gets the patient ID.
     *
     * @return the patient ID
     */
    public int getPatientId() {
        return patientId;
    }
}
