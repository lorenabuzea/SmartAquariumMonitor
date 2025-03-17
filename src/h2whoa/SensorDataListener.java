package h2whoa;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortDataListener;

import javax.swing.*;


public class SensorDataListener implements SerialPortDataListener {
        private TemperatureSensor tempSensor;
        private TurbiditySensor turbSensor;
        private WaterLevelSensor levelSensor;
        private LightSensor lightSensor;

    SerialPort sp;

        public SensorDataListener(TemperatureSensor tempSensor, TurbiditySensor turbSensor, WaterLevelSensor levelSensor, LightSensor lightSensor) {
            this.tempSensor = tempSensor;
            this.turbSensor = turbSensor;
            this.levelSensor = levelSensor;
            this.lightSensor = lightSensor;
        }

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                byte[] newData = event.getReceivedData();
                String dataString = new String(newData).trim();
                System.out.println("Received Data: " + dataString);

                byte[] buffer = new byte[sp.bytesAvailable()];
                int bytesRead = sp.readBytes(buffer, buffer.length);

                if (bytesRead > 0) {

                    String[] readings = dataString.split("\n");
                    for (String reading : readings) {
                        if (reading.startsWith("T:")) {
                            processTemperature(reading.substring(3));
                        } else if (reading.startsWith(",C:")) {
                            processTurbidity(reading.substring(3));
                        } else if (reading.startsWith(",W:")) {
                            processWaterLevel(reading.substring(3));
                        } else if (reading.startsWith(",L:")) {
                            processLight(reading.substring(3));
                        }
                    }
                }
            }
        }

    private void processTemperature(String data) {
        if (TemperatureSensor.isValidTemperatureData(data)) {
            try {
                double temperature = Double.parseDouble(data);
                SwingUtilities.invokeLater(() -> tempSensor.updateTemperature(temperature));
            } catch (NumberFormatException e) {
                System.err.println("Invalid temperature format: " + data);
            }
        } else {
            System.err.println("Temperature data is not valid: " + data);
        }
    }

    private void processTurbidity(String data) {
        if (TurbiditySensor.isValidTurbidityData(data)) {
            SwingUtilities.invokeLater(() -> turbSensor.updateTurbidity(data));
        } else {
            System.err.println("Turbidity data is not valid: " + data);
        }
    }

    private void processWaterLevel(String data) {
        if (WaterLevelSensor.isValidLevelData(data)) {
            try {
                double level = Double.parseDouble(data);
                SwingUtilities.invokeLater(() -> levelSensor.updateWaterLevelSensor(level));
            } catch (NumberFormatException e) {
                System.err.println("Invalid water level format: " + data);
            }
        } else {
            System.err.println("Water level data is not valid: " + data);
        }
    }

    private void processLight(String data) {
        if (LightSensor.isValidLightData(data)) {
            try {
                double light = Double.parseDouble(data);
                SwingUtilities.invokeLater(() -> lightSensor.updateLight(light));
            } catch (NumberFormatException e) {
                System.err.println("Invalid light format: " + data);
            }
        } else {
            System.err.println("Light data is not valid: " + data);
        }
    }
}



