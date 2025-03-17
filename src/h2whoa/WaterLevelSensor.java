package h2whoa;


public class WaterLevelSensor {
        private double level;

        private ColoredTextField textField;

        public WaterLevelSensor(ColoredTextField textField) {
            this.textField = textField;
            this.level =  0.0; // Initial value
        }

        public void updateWaterLevelSensor(double newLevel) {
            level = newLevel;
            double prag = 550.0;
            textField.updateTextBasedOnWaterLevel(level, prag);
        }

        public static boolean isValidLevelData(String data) {
            return data.matches("-?\\d+(\\.\\d+)?");
        }
    }

