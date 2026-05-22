package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlertFactory;
import com.alerts.PriorityAlertDecorator;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks oxygen saturation for low levels and rapid drops.
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    private final BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();

    @Override
    public void checkAlert(Patient patient, List<Alert> generatedAlerts) {
        List<PatientRecord> saturationRecords = new ArrayList<>();

        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())) {
            if (record.getRecordType().equals("Saturation")) {
                saturationRecords.add(record);
                if (record.getMeasurementValue() < 92) {
                    Alert alert = factory.createAlert(String.valueOf(record.getPatientId()), "low saturation", record.getTimestamp());
                    generatedAlerts.add(new PriorityAlertDecorator(alert, "HIGH"));
                }
            }
        }

        checkRapidDrop(saturationRecords, generatedAlerts);
    }

    private void checkRapidDrop(List<PatientRecord> records, List<Alert> generatedAlerts) {
        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                PatientRecord first = records.get(i);
                PatientRecord second = records.get(j);
                long timeDifference = second.getTimestamp() - first.getTimestamp();
                double drop = first.getMeasurementValue() - second.getMeasurementValue();

                if (timeDifference <= 600000 && drop >= 5) {
                    generatedAlerts.add(factory.createAlert(String.valueOf(second.getPatientId()), "rapid saturation drop", second.getTimestamp()));
                    return;
                }
            }
        }
    }
}
