package alerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alerts.Alert;
import com.alerts.BloodPressureAlertFactory;
import com.alerts.PriorityAlertDecorator;
import com.alerts.RepeatedAlertDecorator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

class PatternTest {

    @Test
    void testFactoryCreatesAlert() {
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "critical systolic", 1000L);

        assertEquals("1", alert.getPatientId());
        assertTrue(alert.getCondition().contains("critical systolic"));
    }

    @Test
    void testPriorityDecoratorAddsPriority() {
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "critical systolic", 1000L);
        Alert decorated = new PriorityAlertDecorator(alert, "HIGH");

        assertTrue(decorated.getCondition().contains("HIGH"));
    }

    @Test
    void testRepeatedDecoratorAddsRepeatText() {
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "critical systolic", 1000L);
        Alert decorated = new RepeatedAlertDecorator(alert, 3);

        assertTrue(decorated.getCondition().contains("3"));
    }

    @Test
    void testDataStorageSingleton() {
        assertSame(DataStorage.getInstance(), DataStorage.getInstance());
    }
}
