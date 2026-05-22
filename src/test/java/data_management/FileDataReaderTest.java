package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class FileDataReaderTest {

    @Test
    void testReadSaturationFile() throws Exception {
        Path tempDirectory = Files.createTempDirectory("reader-test");
        Path saturationFile = tempDirectory.resolve("Saturation.txt");
        Files.writeString(saturationFile, "Patient ID: 1, Timestamp: 1000, Label: Saturation, Data: 95%\n");

        DataStorage storage = new DataStorage();
        FileDataReader reader = new FileDataReader(tempDirectory.toString());
        reader.readData(storage);

        assertEquals(1, storage.getRecords(1, 0, 2000).size());
        assertEquals(95.0, storage.getRecords(1, 0, 2000).get(0).getMeasurementValue());
    }
}
