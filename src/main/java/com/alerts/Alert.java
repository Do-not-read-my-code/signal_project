package com.alerts;

/**
 * Common alert contract used by factories and decorators.
 */
public interface Alert {

    /**
     * Gets the patient linked to this alert.
     *
     * @return the patient ID as text
     */
    String getPatientId();

    /**
     * Gets the condition that triggered this alert.
     *
     * @return the alert condition
     */
    String getCondition();

    /**
     * Gets the time this alert was created.
     *
     * @return the alert timestamp
     */
    long getTimestamp();
}
