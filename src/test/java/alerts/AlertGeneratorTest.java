package alerts;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

class AlertGeneratorTest {

    @Test
    void testLowSaturationAlert() {
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 89, "Saturation", System.currentTimeMillis());

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        assertTrue(generator.getGeneratedAlerts().stream().anyMatch(alert -> alert.getCondition().contains("low saturation")));
    }

    @Test
    void testCriticalBloodPressureAlert() {
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 190, "SystolicPressure", System.currentTimeMillis());

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        assertTrue(generator.getGeneratedAlerts().stream().anyMatch(alert -> alert.getCondition().contains("critical")));
    }

    @Test
    void testCombinedAlert() {
        DataStorage storage = new DataStorage();
        long now = System.currentTimeMillis();
        storage.addPatientData(1, 85, "SystolicPressure", now);
        storage.addPatientData(1, 88, "Saturation", now);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        assertTrue(generator.getGeneratedAlerts().stream().anyMatch(alert -> alert.getCondition().contains("hypotensive hypoxemia")));
    }

    @Test
    void testTrendAlert() {
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 100, "SystolicPressure", 1000L);
        storage.addPatientData(1, 115, "SystolicPressure", 2000L);
        storage.addPatientData(1, 130, "SystolicPressure", 3000L);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);

        assertTrue(generator.getGeneratedAlerts().stream().anyMatch(alert -> alert.getCondition().contains("increasing")));
    }
}
