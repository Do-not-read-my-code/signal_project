package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlertFactory;
import com.alerts.PriorityAlertDecorator;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Checks for the combined hypotensive hypoxemia condition.
 */
public class CombinedHypoxemiaStrategy implements AlertStrategy {

    private final BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();

    @Override
    public void checkAlert(Patient patient, List<Alert> generatedAlerts) {
        double latestSystolic = -1;
        double latestSaturation = -1;
        long timestamp = System.currentTimeMillis();

        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())) {
            if (record.getRecordType().equals("SystolicPressure")) {
                latestSystolic = record.getMeasurementValue();
                timestamp = record.getTimestamp();
            }
            if (record.getRecordType().equals("Saturation")) {
                latestSaturation = record.getMeasurementValue();
                timestamp = record.getTimestamp();
            }
        }

        if (latestSystolic < 90 && latestSaturation < 92) {
            Alert alert = factory.createAlert(String.valueOf(patient.getPatientId()), "hypotensive hypoxemia", timestamp);
            generatedAlerts.add(new PriorityAlertDecorator(alert, "CRITICAL"));
        }
    }
}
