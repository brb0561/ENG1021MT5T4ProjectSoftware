import org.firmata4j.Pin;

import java.io.IOException;

public class WaterLevelSensor {
    private static final String PORT = "COM3";
    private static final int SENSOR_POWER = 5;
    private static final int SENSOR_PIN = 14; // A0 is typically pin 14 on Arduino Uno

    Pin waterLevelSensor;

    //
    public WaterLevelSensor(Pin waterLevelSensor) {
        this.waterLevelSensor = waterLevelSensor;

    }

    public void run() throws IOException {
        // Setup
        this.waterLevelSensor.setMode(Pin.Mode.OUTPUT);
        this.waterLevelSensor.setMode(Pin.Mode.ANALOG);
        this.waterLevelSensor.setValue(0); // Ensure it starts LOW
        try {
            while (true) {
                // Turn sensor ON
                this.waterLevelSensor.setValue(1);
                Thread.sleep(1000); // Wait 1 second

                // Read value
                long value = this.waterLevelSensor.getValue();
                String result = "Value: " + value;

                // Calibration logic
                if (value <= 120) {
                    result += " = 0%";
                } else if (value <= 135) {
                    result += " = 0% - 25%";
                } else if (value <= 145) {
                    result += " = 25% - 50%";
                } else if (value <= 150) {
                    result += " = 50% - 75%";
                } else {
                    result += " = 75% - 100%";
                }

                System.out.println(result);

                // Turn sensor OFF
                this.waterLevelSensor.setValue(0);

                // Wait 5 seconds before next loop
                Thread.sleep(5000);
            }
        } catch (InterruptedException ie) {
            System.out.println("can't sleep");
        }
    }
}

