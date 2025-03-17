package h2whoa;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class home_page extends JFrame {


    private StringBuilder accumulatedData = new StringBuilder();

    private TemperatureSensor tempSensor;

    private TurbiditySensor turbSensor;

    private WaterLevelSensor levelSensor;

    private LightSensor lightSensor;
    private SerialPort sp; // Store the SerialPort instance

    home_page() {
        getContentPane().setBackground(new Color(52, 113, 235));
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/sticker_hp.png"));
        Image i = i1.getImage().getScaledInstance(650, 500, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i);
        JLabel img = new JLabel(i2);
        img.setBounds(450, 0, 600, 500);
        add(img);

        JLabel heading = new JLabel("H2WHOA HomePage");
        heading.setBounds(70, 60, 400, 45);
        heading.setFont(new Font("Copperplate", Font.BOLD, 30));
        heading.setForeground(Color.white);
        add(heading);

        ColoredTextField tempTextField = new ColoredTextField(20);
        tempTextField.setBounds(120, 120, 200, 30);
        add(tempTextField);

        tempSensor = new TemperatureSensor(tempTextField);

        ColoredTextField turbTextField = new ColoredTextField(20);
        turbTextField.setBounds(120, 200, 200, 30);
        add(turbTextField);

        turbSensor = new TurbiditySensor(turbTextField);

        ColoredTextField levelTextField = new ColoredTextField(20);
        levelTextField.setBounds(120, 280, 200, 30);
        add(levelTextField);

        levelSensor = new WaterLevelSensor(levelTextField);

        ColoredTextField lightTextField = new ColoredTextField(20);
        lightTextField.setBounds(120, 360, 200, 30);
        add(lightTextField);

        lightSensor = new LightSensor(lightTextField);

        setSize(1000, 500);
        setLocation(200, 150);
        setVisible(true);



        // Create and configure the SerialPort instance
        sp = SerialPort.getCommPort("/dev/cu.usbmodem1101");
        sp.setComPortParameters(9600, 35, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY); //am shcimbat newDataBits ;aci din byte.size
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (!sp.openPort()) {
            throw new IllegalStateException("Failed to open serial port");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(sp::closePort));

        var timer = new Timer();
        var timedSchedule = new TimerScheduleHandler(System.currentTimeMillis());

        // Add a data listener to update the temperature in the home_page instance
        sp.addDataListener(new MyDataListener());
        //sp.addDataListener(new SensorDataListener(tempSensor, turbSensor, levelSensor, lightSensor));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Close the serial port when the window is closing
                if (sp != null && sp.isOpen()) {
                    sp.closePort();
                }
                System.exit(0); // Exit the application
            }
        });



        System.out.println("Listen: " + timedSchedule.getListeningEvents());
        timer.schedule(timedSchedule, 0, 1000);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(home_page::new);
    }

    private class MyDataListener implements com.fazecast.jSerialComm.SerialPortDataListener {
        @Override
        public int getListeningEvents() {
            return com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        }

        public void serialEvent(com.fazecast.jSerialComm.SerialPortEvent event) {
            if (event.getEventType() != com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                return;

            byte[] buffer = new byte[sp.bytesAvailable()];
            int bytesRead = sp.readBytes(buffer, buffer.length);

            if (bytesRead > 0) {
                String receivedData = new String(buffer, 0, bytesRead);
                accumulatedData.append(receivedData);

                // Process data if end of line is found
                if (accumulatedData.toString().contains("\n")) {
                    processData(accumulatedData.toString());
                    accumulatedData.setLength(0); // Clear the accumulator
                }
            }
        }


        private void processData(String data) {
            // Split the accumulated data into lines
            String[] lines = data.split("\n");

            // Iterate over each line
            for (String line : lines) {
                // Further processing of each line
                processLine(line);
            }
        }

        private void processLine(String line) {
            // Split the line by commas to get each sensor reading
            String[] readings = line.split(",");

            // Process each reading
            for (String reading : readings) {
                if (reading.startsWith("T:")) {
                    processTemperature(reading.substring(2));
                } else if (reading.startsWith("C:")) {
                    processTurbidity(reading.substring(2));
                } else if (reading.startsWith("W:")) {
                    processWaterLevel(reading.substring(2));
                } else if (reading.startsWith("L:")) {
                    processLight(reading.substring(2));
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
            System.err.println("Light data is not valid:" + data);
        }
    }
    }
}


