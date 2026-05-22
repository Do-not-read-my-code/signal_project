package com.alerts;

/**
 * Factory Method base class for creating alerts.
 */
public abstract class AlertFactory {

    /**
     * Creates an alert for a patient.
     *
     * @param patientId the patient ID
     * @param condition the condition text
     * @param timestamp the time the alert was triggered
     * @return the created alert
     */
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}
