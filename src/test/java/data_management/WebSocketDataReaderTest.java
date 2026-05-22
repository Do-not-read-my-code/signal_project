package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketDataReader;
import java.util.List;
import org.junit.jupiter.api.Test;

class WebSocketDataReaderTest {

    @Test
    void testValidWebSocketMessageIsStored() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.setDataStorage(storage);
        reader.handleMessage("1,1714376789050,Saturation,91%");

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789050L);
        assertEquals(1, records.size());
        assertEquals("Saturation", records.get(0).getRecordType());
        assertEquals(91.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testInvalidWebSocketMessageIsIgnored() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.setDataStorage(storage);
        reader.handleMessage("this is not valid data");

        assertEquals(0, storage.getAllPatients().size());
    }

    @Test
    void testBooleanAlertMessageIsStoredAsNumber() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.setDataStorage(storage);
        reader.handleMessage("2,1714376789051,Alert,true");

        List<PatientRecord> records = storage.getRecords(2, 1714376789051L, 1714376789051L);
        assertEquals(1, records.size());
        assertEquals(1.0, records.get(0).getMeasurementValue());
    }
}
