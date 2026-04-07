import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class cycle extends TimerTask {
    private final double tankThreshold = 2.3; //
    Pin waterPump;
    Pin servo;
    Pin waterSensor;
    Pin led;
    SSD1306 oled;
    //Pin buzzer;
    Pin tankSensor;
    FirmataDevice arduino;
    Pin button;

    final String foodTrackFile = "FoodTrack.txt";
    final String timeTextFile = "Time.txt";
    final double waterThreshold = 2.5;// Threshold voltage value
    final String datePattern = "yyyy-MM-dd HH:mm:ss";

    public cycle(FirmataDevice arduino, Pin button, Pin waterPump, Pin motor, Pin waterSensor, Pin tankSensor, Pin led, SSD1306 oled){//removed buzzer
        this.servo = motor;
        this.waterPump = waterPump;
        this.waterSensor = waterSensor;
        this.tankSensor = tankSensor;
        this.led = led;
        this.oled = oled;
        this.arduino = arduino;
        //this.buzzer =buzzer;
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
            // --- TANK LEVEL SENSOR --- (TURNS OFF PUMP AND DISPLAYS ALARM MESSAGE)
            double tankLevel = readWaterLevel(this.tankSensor);
            if (tankLevel < tankThreshold) {
                System.out.println("CRITICAL: Tank Empty. System Locked.");
                this.waterPump.setValue(0); // Safety: kill pump immediately
                waterLevelAlarm();          // Trigger your alarm/OLED message
                return;                     // EXIT the method: nothing below will run
            }

            // --- WATER BOWL SENSOR --- (WORKS WITH PUMP)
            double initialCheck = readWaterLevel(waterSensor);
            if (initialCheck < waterThreshold) {
                System.out.println("Bowl low. Starting Pump...");
                // Calling readWaterLevel(this.waterSensor) INSIDE the while condition
                // so it gets a fresh value every time the loop repeats.
                int safetyCounter = 0;
                while ((readWaterLevel(waterSensor) < waterThreshold) && safetyCounter < 10) {
                    waterPump.setValue(1); // Keep pump running
                    Thread.sleep(500);          // Wait half a second
                    safetyCounter++;            // Prevent infinite pumping
                }
                waterPump.setValue(0); // SHUT OFF IMMEDIATELY
            }

            startTime = System.currentTimeMillis();
            // start at 0
            servo.setValue(0);
            Thread.sleep(2000);

            arduino.addEventListener(new buttonDetector(button, servo));
            double flowRate = 1.13;//flow rate of food dispensed in grams/s

            elapsedTime = System.currentTimeMillis() - startTime;//timer to account for the amount of time that water & amount of food is dispensed
            double amountOfFood = (flowRate*elapsedTime);
            writeToFood(amountOfFood, startTime);//amount of food dispensed to txt file using buffered writer.

            //waterLevelCheck needs to be added for owner to know if the container is empty (add a threshold value)

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
    public void writeToFood(double amountOfFood, long time){
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

    Status:Complete
    Goes off if the detected water level is lower than required

 */
    public void waterLevelAlarm() throws IOException, InterruptedException {

        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);
        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);
        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);
        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);

        this.oled.clear();
        this.oled.getCanvas().write("PLEASE REFILL THE WATER TANK");
        this.oled.display();

        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);
        Thread.sleep(100);
        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);
        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);
        this.led.setValue(1);
        Thread.sleep(100);
        this.led.setValue(0);
    }
}
