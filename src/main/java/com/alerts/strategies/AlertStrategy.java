package com.alerts.strategies;

import com.alerts.Alert;
import com.data_management.Patient;
import java.util.List;

/**
 * Strategy interface for deciding whether patient data should trigger alerts.
 */
public interface AlertStrategy {

    /**
     * Checks one patient and appends any triggered alerts to the list.
     *
     * @param patient         the patient to check
     * @param generatedAlerts the list to add new alerts to
     */
    void checkAlert(Patient patient, List<Alert> generatedAlerts);
}
