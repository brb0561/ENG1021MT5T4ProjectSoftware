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
        final int ledPin = 4;//D4
        final int buzzerPin = 5;//D5



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
        Pin led = arduino.getPin(ledPin);
        Pin buzzer = arduino.getPin(buzzerPin);
        buzzer.setMode(Pin.Mode.OUTPUT);
        led.setMode(Pin.Mode.OUTPUT);
        waterLevelSensor.setMode(Pin.Mode.ANALOG);
        button.setMode(Pin.Mode.INPUT);
        //add device startups for waterpump and motor
        waterPump.setMode(Pin.Mode.OUTPUT); // Allows the pump to receive power
        // Initial safety: make sure they are OFF at startup
        waterPump.setValue(0);

        arduino.addEventListener(new buttonDetector(button,waterPump));//button detection for dispensing food.

        I2CDevice i2cObject = arduino.getI2CDevice((byte) 0x3C);
        SSD1306 oledDisplay = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        oledDisplay.init();

        var cycle = new cycle(waterPump,motor, waterLevelSensor,led,oledDisplay, buzzer);
        new Timer().schedule(cycle,0,cycleDuration);
    }


}
