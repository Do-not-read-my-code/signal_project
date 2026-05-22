package com.alerts;

/**
 * Creates manual alerts.
 */
public class ManualAlertFactory extends AlertFactory {

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BasicAlert(patientId, "Manual: " + condition, timestamp);
    }
}
