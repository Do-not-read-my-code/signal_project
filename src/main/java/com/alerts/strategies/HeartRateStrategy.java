package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Checks ECG values for large peaks compared to the patient's average.
 */
public class HeartRateStrategy implements AlertStrategy {

    private final ECGAlertFactory factory = new ECGAlertFactory();

    @Override
    public void checkAlert(Patient patient, List<Alert> generatedAlerts) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
        double total = 0;
        int count = 0;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                total += record.getMeasurementValue();
                count++;
            }
        }

        if (count == 0) {
            return;
        }

        double average = total / count;
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG") && record.getMeasurementValue() > average * 1.8) {
                generatedAlerts.add(factory.createAlert(String.valueOf(record.getPatientId()), "abnormal ECG peak", record.getTimestamp()));
                return;
            }
        }
    }
}
