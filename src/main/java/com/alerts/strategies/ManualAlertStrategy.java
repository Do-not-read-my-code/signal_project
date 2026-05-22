package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.ManualAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Checks for manually triggered alerts from the simulator.
 */
public class ManualAlertStrategy implements AlertStrategy {

    private final ManualAlertFactory factory = new ManualAlertFactory();

    @Override
    public void checkAlert(Patient patient, List<Alert> generatedAlerts) {
        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())) {
            if (record.getRecordType().equals("Alert") && record.getMeasurementValue() == 1.0) {
                generatedAlerts.add(factory.createAlert(String.valueOf(record.getPatientId()), "button triggered", record.getTimestamp()));
            }
        }
    }
}
