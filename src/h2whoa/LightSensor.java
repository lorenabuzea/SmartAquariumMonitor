package h2whoa;

public class LightSensor {
    private double light;
    private ColoredTextField textField;

    public LightSensor(ColoredTextField textField) {
        this.textField = textField;
        this.light = 0.0; // Initial value
    }

    public void updateLight(double newLight) {
        light = newLight;
        double prag = 450;
        textField.updateTextBasedOnLight(light, prag);
    }

    public static boolean isValidLightData(String data) {
        try {
            Double.parseDouble(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }    }
}
