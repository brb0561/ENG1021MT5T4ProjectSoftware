/*
FileName:Driver
About:The driver program for the arduino hardware
Created by:
Date Modified:2/3/2026
 */

import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.Timer;
public class Driver {
    /*
    Method Name:Main
    About:The main driver
     */

    static final String portNumber = "COM3";
    public static void main(String[] args) throws IOException {
        final int waterLevelSensorPin = 14;//A0
        final int waterPumPin = 7; //D7
        final int buttonPin = 6;//D6

        FirmataDevice arduino = new FirmataDevice(portNumber);
        arduino.start();
        System.out.println("Board Started");
        try {
            arduino.ensureInitializationIsDone();
        } catch (Exception e) {
            System.out.println("unable to connect to board");
        }
        Pin waterLevelSensor = arduino.getPin(waterLevelSensorPin);
        long readWaterLevelValue  = WaterLevelSensor.run(waterLevelSensor);

    }
/*
Goes off if the detected water level is lower than required
 */
    public static void alarm(int interval){

    }
    public int weightCalc(){
        int weight = 0;
        return weight;
    }
}
