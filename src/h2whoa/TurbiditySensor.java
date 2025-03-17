package h2whoa;

import javax.swing.*;

public class TurbiditySensor {
    private ColoredTextField textField;

    public TurbiditySensor(ColoredTextField textField) {
        this.textField = textField;
    }

    public void updateTurbidity(String newTurbidity) {
        textField.updateTextBasedOnTurbidity( newTurbidity);
    }

    public static boolean isValidTurbidityData(String data) {
        return data.equals("Clear ") || data.equals("Turbid");
    }
}
