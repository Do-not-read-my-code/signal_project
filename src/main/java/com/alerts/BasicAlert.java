package com.alerts;

/**
 * Basic implementation of an alert.
 */
public class BasicAlert implements Alert {

    private final String patientId;
    private final String condition;
    private final long timestamp;

    /**
     * Creates a basic alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the time the alert was created
     */
    public BasicAlert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
