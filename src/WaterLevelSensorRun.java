import org.firmata4j.Pin;

import java.io.IOException;

public class WaterLevelSensorRun {

    public static double readWaterLevel(Pin waterLevelSensor) throws IOException {
    waterLevelSensor.setMode(Pin.Mode.ANALOG);
    return convertWaterLevelValue(waterLevelSensor.getValue());
    }
    /*
    Name: convertWaterLevelValue
    About:Converts the value from the board (long) to a readable voltage.
     */
    public static double convertWaterLevelValue(long waterLevel){
        return ((waterLevel/1023.0)*5.0);//converts raw value to a readable voltage
    }

}

