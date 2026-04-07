import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;

public class buttonDetector implements IODeviceEventListener{


    //Status:Completed
    //About:Just detects if the button is pressed or not to dispense food.

        private  final Pin servo;
        private final FirmataDevice arduino;
        final int foodButtonPin = 2;//D2

        private final Pin buttonPin;
        // constructor
        buttonDetector(FirmataDevice arduino, Pin buttonPin, Pin servo) {
            this.servo = servo;
            this.arduino = arduino;
            this.buttonPin=buttonPin;
        }

        @Override
        public void onPinChange(IOEvent event) {
                for (int i = 0; i < 3; i++) {
                    try {
                        servo.setValue(180);   // go to 180 degrees
                        Thread.sleep(650);
                        servo.setValue(0);     // go back to 0 degrees
                        Thread.sleep(650);

                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }


        }
        // These are empty methods (nothing in the curly braces)
        @Override
        public void onStart(IOEvent event) {}
        @Override
        public void onStop(IOEvent event) {}
        @Override
        public void onMessageReceive(IOEvent event, String message) {}
    }




