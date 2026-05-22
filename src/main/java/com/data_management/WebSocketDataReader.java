package com.data_management;

import java.io.IOException;
import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Reads live patient data from a WebSocket server.
 *
 * <p>The simulator sends WebSocket messages in this format:
 * {@code patientId,timestamp,label,data}. Each incoming message is parsed and
 * stored in {@link DataStorage} as a {@link PatientRecord}.</p>
 */
public class WebSocketDataReader extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;

    /**
     * Creates a WebSocket data reader for the given server URI.
     *
     * @param serverUri the WebSocket server URI, for example {@code ws://localhost:8080}
     */
    public WebSocketDataReader(String serverUri) {
        super(URI.create(serverUri));
    }

    /**
     * Connects to the WebSocket server and stores future incoming data in the given storage.
     *
     * @param dataStorage the storage used for parsed patient records
     * @throws IOException if the connection cannot be started
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;

        try {
            if (!isOpen()) {
                connect();
            }
        } catch (Exception e) {
            throw new IOException("Could not connect to WebSocket server", e);
        }
    }

    /**
     * Handles a message received from the WebSocket server.
     *
     * @param message the incoming WebSocket message
     */
    @Override
    public void onMessage(String message) {
        handleMessage(message);
    }

    /**
     * Sets the storage used by this reader without opening a WebSocket connection.
     *
     * <p>This is useful for unit tests and for cases where the connection is managed elsewhere.</p>
     *
     * @param dataStorage the storage used for parsed records
     */
    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Parses a WebSocket message and adds it to storage.
     *
     * <p>This method is public so it can be tested without starting a real WebSocket server.</p>
     *
     * @param message the message to parse
     */
    public void handleMessage(String message) {
        if (dataStorage == null) {
            return;
        }

        try {
            String[] parts = message.split(",", 4);
            if (parts.length != 4) {
                throw new IllegalArgumentException("Expected 4 comma-separated fields");
            }

            int patientId = Integer.parseInt(parts[0].trim());
            long timestamp = Long.parseLong(parts[1].trim());
            String label = parts[2].trim();
            double value = parseMeasurement(parts[3].trim());

            dataStorage.addPatientData(patientId, value, label, timestamp);
        } catch (Exception e) {
            System.err.println("Skipping invalid WebSocket message: " + message);
        }
    }

    private double parseMeasurement(String data) {
        String cleanedData = data.replace("%", "").trim();

        if (cleanedData.equalsIgnoreCase("true")) {
            return 1.0;
        }
        if (cleanedData.equalsIgnoreCase("false")) {
            return 0.0;
        }

        return Double.parseDouble(cleanedData);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to WebSocket server");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }
}
