import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class cycle extends TimerTask {
    Pin waterPump;
    Pin motor;
    Pin waterSensor;
    Pin led;
    SSD1306 oled;
    Pin buzzer;

    final String foodTrackFile = "FoodTrack.txt";
    final String timeTextFile = "Time.txt";
    final double waterThreshold = 2.3;// Threshold voltage value
    final String datePattern = "yyyy-MM-dd HH:mm:ss";

    public cycle(Pin waterPump, Pin motor, Pin waterSensor, Pin led, SSD1306 oled, Pin buzzer){
        this.motor = motor;
        this.waterPump = waterPump;
        this.waterSensor = waterSensor;
        this.led = led;
        this.oled = oled;
        this.buzzer =buzzer;
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
            double initialCheck = readWaterLevel(this.waterSensor);
            if (initialCheck < waterThreshold) {
                System.out.println("Bowl low. Starting Pump...");
                // Calling readWaterLevel(this.waterSensor) INSIDE the while condition
                // so it gets a fresh value every time the loop repeats.
                int safetyCounter = 0;
                while (readWaterLevel(this.waterSensor) < waterThreshold && safetyCounter < 10) {
                    this.waterPump.setValue(1); // Keep pump running
                    Thread.sleep(500);          // Wait half a second
                    safetyCounter++;            // Prevent infinite pumping
                }
                this.waterPump.setValue(0); // SHUT OFF IMMEDIATELY
            }

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
        long rawValue = waterLevelSensor.getValue();
        return (rawValue / 1023.0) * 5.0;
    }
    /*
    Name:writeToFood
    About:Writes amount of food data & time it occurred to a text file
    @Parameters:
    - String amountOfFood:The amount of food dispensed
    - long time: time it occurred

   Status:Incomplete
     */
    public void writeToFood(String amountOfFood, long time){
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        date = new Date(time);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(foodTrackFile), 1);
            bufferedWriter.write("Amount of Food: " + amountOfFood+"   Date: " + sdf.format(date));
            bufferedWriter.newLine();
            bufferedWriter.close();
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

 */
    public void waterLevelAlarm() throws IOException, InterruptedException {

        this.buzzer.setValue(1);
        this.led.setValue(1);
        Thread.sleep(100);
        this.buzzer.setValue(0);
        this.led.setValue(0);


        this.oled.clear();
        this.oled.getCanvas().write("PLEASE REFILL THE WATER TANK");
        this.oled.display();

        Thread.sleep(100);
        this.buzzer.setValue(1);
        this.led.setValue(1);
        Thread.sleep(100);
        this.buzzer.setValue(0);
        this.led.setValue(0);
    }
}
