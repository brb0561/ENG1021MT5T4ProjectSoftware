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
        final int tankSensorPin = 16; //A2
        final int waterPumPin = 7; //D7
        final int waterbuttonPin = 6;//D6
        final int servoPin = 3;//temporary pin needs to be adjusted for grove board
        final int ledPin = 4;//D4
        final int buzzerPin = 5;
        final int foodButtonPin = 2;//D2



        FirmataDevice arduino = new FirmataDevice(portNumber);
        arduino.start();
        System.out.println("Board Started");
        try {
            arduino.ensureInitializationIsDone();
        } catch (Exception e) {
            System.out.println("unable to connect to board");
        }
        //initialization of pins/devices.
        Pin waterButton = arduino.getPin(waterbuttonPin);
        Pin waterPump  = arduino.getPin(waterPumPin);
        Pin servo = arduino.getPin(servoPin);
        Pin waterLevelSensor = arduino.getPin(waterLevelSensorPin);
        Pin tankSensor = arduino.getPin(tankSensorPin);
        Pin led = arduino.getPin(ledPin);
        Pin buzzer = arduino.getPin(buzzerPin);
        Pin foodButton = arduino.getPin(foodButtonPin);
        foodButton.setMode(Pin.Mode.INPUT);
        buzzer.setMode(Pin.Mode.OUTPUT);
        led.setMode(Pin.Mode.OUTPUT);
        waterLevelSensor.setMode(Pin.Mode.ANALOG);
        tankSensor.setMode(Pin.Mode.ANALOG);
        waterButton.setMode(Pin.Mode.INPUT);
        //add device startups for waterpump (done) and motor (remaining)
        waterPump.setMode(Pin.Mode.OUTPUT); // Allows the pump to receive power
        // Initial safety: make sure waterpump (done) and motor (remaining) are OFF at startup
        waterPump.setValue(0);

        arduino.addEventListener(new buttonDetector(arduino,servo,servo));//button detection for dispensing water.


        I2CDevice i2cObject = arduino.getI2CDevice((byte) 0x3C);
        SSD1306 oledDisplay = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        oledDisplay.init();

        var cycle = new cycle(waterPump,servo,waterLevelSensor,tankSensor,buzzer,led,oledDisplay);
        new Timer().schedule(cycle,0,cycleDuration);
    }


}
