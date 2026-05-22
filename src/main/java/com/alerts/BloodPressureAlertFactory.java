package com.alerts;

/**
 * Creates blood pressure alerts.
 */
public class BloodPressureAlertFactory extends AlertFactory {

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BasicAlert(patientId, "Blood pressure: " + condition, timestamp);
    }
}
