package com.alerts.strategies;

import java.util.ArrayList;
import java.util.List;

import com.alerts.Alert;
import com.alerts.BloodPressureAlertFactory;
import com.alerts.PriorityAlertDecorator;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Checks blood pressure threshold and trend alerts.
 */
public class BloodPressureStrategy implements AlertStrategy {

    private final BloodPressureAlertFactory factory = new BloodPressureAlertFactory();

    @Override
    public void checkAlert(Patient patient, List<Alert> generatedAlerts) {
        List<PatientRecord> systolic = new ArrayList<>();
        List<PatientRecord> diastolic = new ArrayList<>();

        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())) {
            if (record.getRecordType().equals("SystolicPressure")) {
                systolic.add(record);
                checkCritical(record, true, generatedAlerts);
            }
            if (record.getRecordType().equals("DiastolicPressure")) {
                diastolic.add(record);
                checkCritical(record, false, generatedAlerts);
            }
        }

        checkTrend(systolic, "systolic", generatedAlerts);
        checkTrend(diastolic, "diastolic", generatedAlerts);
    }

    private void checkCritical(PatientRecord record, boolean systolic, List<Alert> generatedAlerts) {
        double value = record.getMeasurementValue();
        boolean dangerous = systolic ? value > 180 || value < 90 : value > 120 || value < 60;

        if (dangerous) {
            Alert alert = factory.createAlert(String.valueOf(record.getPatientId()), "critical " + record.getRecordType(), record.getTimestamp());
            generatedAlerts.add(new PriorityAlertDecorator(alert, "HIGH"));
        }
    }

    private void checkTrend(List<PatientRecord> records, String label, List<Alert> generatedAlerts) {
        if (records.size() < 3) {
            return;
        }

        PatientRecord a = records.get(records.size() - 3);
        PatientRecord b = records.get(records.size() - 2);
        PatientRecord c = records.get(records.size() - 1);
        double firstChange = b.getMeasurementValue() - a.getMeasurementValue();
        double secondChange = c.getMeasurementValue() - b.getMeasurementValue();

        if (firstChange > 10 && secondChange > 10) {
            generatedAlerts.add(factory.createAlert(String.valueOf(c.getPatientId()), "increasing " + label + " trend", c.getTimestamp()));
        }
        if (firstChange < -10 && secondChange < -10) {
            generatedAlerts.add(factory.createAlert(String.valueOf(c.getPatientId()), "decreasing " + label + " trend", c.getTimestamp()));
        }
    }
}
