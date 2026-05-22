package com.alerts;

import com.alerts.strategies.AlertStrategy;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.CombinedHypoxemiaStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.ManualAlertStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates patient data using alert strategies.
 */
public class AlertGenerator {

    private DataStorage dataStorage;
    private List<Alert> generatedAlerts;
    private List<AlertStrategy> strategies;

    /**
     * Creates an alert generator for the given data storage.
     *
     * @param dataStorage the patient data storage to pull patients from
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.generatedAlerts = new ArrayList<>();
        this.strategies = new ArrayList<>();
        this.strategies.add(new BloodPressureStrategy());
        this.strategies.add(new OxygenSaturationStrategy());
        this.strategies.add(new HeartRateStrategy());
        this.strategies.add(new ManualAlertStrategy());
        this.strategies.add(new CombinedHypoxemiaStrategy());
    }

    /**
     * Runs all strategies on one patient.
     *
     * @param patient the patient to evaluate
     */
    public void evaluateData(Patient patient) {
        for (AlertStrategy strategy : strategies) {
            strategy.checkAlert(patient, generatedAlerts);
        }
    }

    /**
     * Runs all strategies on every patient in storage.
     */
    public void evaluateAllPatients() {
        for (Patient patient : dataStorage.getAllPatients()) {
            evaluateData(patient);
        }
    }

    /**
     * Gets all alerts generated so far.
     *
     * @return the list of generated alerts
     */
    public List<Alert> getGeneratedAlerts() {
        return generatedAlerts;
    }

    /**
     * Adds an alert manually.
     *
     * @param alert the alert to add
     */
    public void triggerAlert(Alert alert) {
        generatedAlerts.add(alert);
        System.out.println("Alert for patient " + alert.getPatientId() + ": " + alert.getCondition());
    }
}
