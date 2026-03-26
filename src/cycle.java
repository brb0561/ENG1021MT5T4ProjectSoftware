import org.firmata4j.Pin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        long startTime;
        double elapsedTime = 0;
        String timeTextFile = "Time.txt";
        String foodTrackFile = "FoodTrack.txt";
        try {
            BufferedWriter bufferedWriterForDate = new BufferedWriter(new FileWriter(timeTextFile));
            BufferedWriter bufferedWriterForFood  = new BufferedWriter(new FileWriter(foodTrackFile));

            while (readWaterLevel(this.waterSensor)<waterThreshold){//keeps pumping water till it reaches acceptable threshold.
                this.waterSensor.setValue(1);
                Thread.sleep(2000);
                this.waterSensor.setValue(0);
            }
            startTime = System.currentTimeMillis();
            //add motor spinning code here + time it took.
            //add flowrate of food & time to calculate amount of food dispensed.
            //add amount of food dispensed to txt file using buffered writer.

            elapsedTime = System.currentTimeMillis()-startTime;
            bufferedWriterForDate.write(""+elapsedTime);
            bufferedWriterForDate.newLine();
            bufferedWriterForDate.close();
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
