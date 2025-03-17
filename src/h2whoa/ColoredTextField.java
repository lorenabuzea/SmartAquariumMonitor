package h2whoa;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ColoredTextField extends JTextField {
    private Color optimalColor = Color.GREEN; // Color for optimal temperature
    private Color warningColor = Color.RED;   // Color for low or high temperature

    public ColoredTextField(int columns) {
        super(columns);
        setDefaultColor(); // Set the default text color
    }

    public ColoredTextField(int columns, String initialText) {
        super(columns);
        setText(initialText); // Set the initial text
        setDefaultColor(); // Set the default text color
    }

    public void updateTextBasedOnTemperature(double temperature, double lowBound, double highBound) {
        if (temperature >= lowBound && temperature <= highBound) {
            setText(" \uD83C\uDD97 Temperature is OPTIMAL");
            setForeground(optimalColor);
        } else if (temperature < lowBound) {
            setText("❗Temperature is LOW");
            setForeground(warningColor);
        } else { // Assumes temperature is high
            setText("❗Temperature is HIGH");
            setForeground(warningColor);
        }
    }

    public void updateTextBasedOnTurbidity(String turbidity) {
        if (Objects.equals(turbidity, "Clear ")) {
            setText(" \uD83C\uDD97 Water is CLEAR");
            setForeground(optimalColor);
        } else if(Objects.equals(turbidity, "Turbid")){
            setText("❗Water needs to be changed");
            setForeground(warningColor);
        }
    }

    public void updateTextBasedOnWaterLevel(double level, double prag) {
        if (level >= prag) {
            setText(" \uD83C\uDD97 Water level is OPTIMAL");
            setForeground(optimalColor);
        } else {
            setText("❗Water level is LOW");
            setForeground(warningColor);
        }
    }

    public void updateTextBasedOnLight(double light, double prag) {
        if (light >= prag) {
            setText(" \uD83C\uDD97 Luminosity is OPTIMAL");
            setForeground(optimalColor);
        } else {
            setText("❗Luminosity is LOW");
            setForeground(warningColor);
        }
    }

    private void setDefaultColor() {
        setForeground(Color.BLACK); // Default text color
    }

}
