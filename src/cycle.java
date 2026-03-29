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
    final String foodTrackFile = "FoodTrack.txt";
    final String timeTextFile = "Time.txt";
    final double waterThreshold = 30;//needs to be adjusted
    public cycle(Pin waterPump, Pin motor, Pin waterSensor){
        this.motor = motor;
        this.waterPump = waterPump;
        this.waterSensor = waterSensor;
    }
    @Override
    /*
    Name:run
    About:runs the cycle.
    Status:Incomplete
     */
    public void run(){
        long startTime;
        double elapsedTime = 0;
        try {

            startTime = System.currentTimeMillis();
            //add motor spinning code here + time it took.
            //add flowrate of food & time to calculate amount of food dispensed.
            //add amount of food dispensed to txt file using buffered writer.
            elapsedTime = System.currentTimeMillis() - startTime;//timer to account for the amount of time that water & amount of food is dispensed
            writeToFood("#amountoffood, needs to be adjusted to a variable", startTime);

            //waterLevelCheck needs to be added for owner to know if the container is empty (add a threshold value)

            if (readWaterLevel(this.waterSensor)<waterThreshold){
                waterLevelAlarm();
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    Name: readWaterLevel
    About:Reads the current water level and returns the voltage level
    Status:Complete
     */

    public static double readWaterLevel(Pin waterLevelSensor) throws IOException, InterruptedException {
        waterLevelSensor.setValue(1);
        long waterSensorValue = waterLevelSensor.getValue();
        Thread.sleep(100);
        waterLevelSensor.setValue(0);//turns sensor off to prevent corrosion?
        return ((waterSensorValue/1023.0)*5.0);//converts the given waterLevelSensor value into a readable voltage value
    }
    /*
    Name:writeToFood
    About:Writes amount of food data & time it occurred to a text file
    @Parameters:
    - String amountOfFood:The amount of food dispensed
    - double time: time it occurred

   Status:Incomplete
     */
    public void writeToFood(String amountOfFood, double time){
        try {
            BufferedWriter bufferedWriterForFood = new BufferedWriter(new FileWriter(foodTrackFile), 1);
            BufferedWriter bufferedWriterForDate = new BufferedWriter(new FileWriter(timeTextFile), 1);
            bufferedWriterForDate.write("" + time);
            bufferedWriterForDate.newLine();
            bufferedWriterForDate.close();
        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    /*
    Name:waterLevelAlarm
    About:Sets an alarm off for the user to refill water?
    Parameters:

    Status:Incomplete
    Goes off if the detected water level is lower than required
    Runs for 10 seconds each cycle if the water level is below?

 */
    public static void waterLevelAlarm(int interval){

    }
}
