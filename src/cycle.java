import org.firmata4j.Pin;

import java.io.IOException;
import java.util.TimerTask;

public class cycle extends TimerTask {
    Pin waterPump;
    Pin motor;
    Pin waterSensor;
    final double waterThreshold = 30;//needs to be adjusted
    public cycle(Pin waterPump, Pin motor, Pin waterSensor){
        this.motor = motor;
        this.waterPump = waterPump;
        this.waterSensor = waterSensor;
    }
    @Override
    public void run(){
        try {
            while (readWaterLevel(this.waterSensor)<waterThreshold){//keeps pumping water till it reaches acceptable threshold.
                this.waterSensor.setValue(1);
                Thread.sleep(2000);
                this.waterSensor.setValue(0);
            }
            //add motor spinning code here.
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static double readWaterLevel(Pin waterLevelSensor) throws IOException {
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
