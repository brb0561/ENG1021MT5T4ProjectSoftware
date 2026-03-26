import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import java.io.IOException;

public class WaterLevelSensor {
    private static final String PORT = "COM3";
    private static final int SENSOR_POWER = 5;
    private static final int SENSOR_PIN = 14; // A0 is typically pin 14 on Arduino Uno

    public static void main(String[] args) throws Exception {
        var device = new FirmataDevice(PORT);

        try {
            device.start();
            device.ensureInitializationIsDone();

            var powerPin = device.getPin(SENSOR_POWER);
            var analogPin = device.getPin(SENSOR_PIN);

            // Setup
            powerPin.setMode(Pin.Mode.OUTPUT);
            analogPin.setMode(Pin.Mode.ANALOG);
            powerPin.setValue(0); // Ensure it starts LOW

            while (true) {
                // Turn sensor ON
                powerPin.setValue(1);
                Thread.sleep(1000); // Wait 1 second

                // Read value
                long value = analogPin.getValue();
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
                powerPin.setValue(0);

                // Wait 5 seconds before next loop
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            device.stop();
        }
    }
}
