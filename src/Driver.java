/*
FileName:Driver
About:The driver program for the arduino hardware
Created by:
Date Modified:2/3/2026
 */

import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class Driver {
    /*
    Method Name:Main
    About:The main driver
     */

    static final String portNumber = "COM3";
    public static void main(String[] args) throws IOException {
        final int cycleDuration  = 4000;//
        final int waterLevelSensorPin = 14;//A0
        final int waterPumPin = 7; //D7
        final int buttonPin = 6;//D6
        final int motorPin = 0;//temporary pin needs to be adjusted for grove board



        FirmataDevice arduino = new FirmataDevice(portNumber);
        arduino.start();
        System.out.println("Board Started");
        try {
            arduino.ensureInitializationIsDone();
        } catch (Exception e) {
            System.out.println("unable to connect to board");
        }
        //initialization of pins/devices.
        Pin button = arduino.getPin(buttonPin);
        Pin waterPump  = arduino.getPin(waterPumPin);
        Pin motor = arduino.getPin(motorPin);
        Pin waterLevelSensor = arduino.getPin(waterLevelSensorPin);
        waterLevelSensor.setMode(Pin.Mode.ANALOG);
        arduino.addEventListener(new buttonDetector(button,waterPump));//button detection for dispensing food.
        var cycle = new cycle(waterPump,motor, waterLevelSensor);
        new Timer().schedule(cycle,0,cycleDuration);
    }


}
