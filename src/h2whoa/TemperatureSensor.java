package h2whoa;

public class TemperatureSensor {
    private double temperature;
    private ColoredTextField textField;

    public TemperatureSensor(ColoredTextField textField) {
        this.textField = textField;
        this.temperature = 0.0; // Initial value
    }

    public void updateTemperature(double newTemperature) {
        temperature = newTemperature;
        double lowTemp = 18.0;
        double highTemp = 24.0;
        textField.updateTextBasedOnTemperature(temperature, lowTemp, highTemp);
    }

    public static boolean isValidTemperatureData(String data) {
        return data.matches("-?\\d+(\\.\\d+)?");
    }
}

