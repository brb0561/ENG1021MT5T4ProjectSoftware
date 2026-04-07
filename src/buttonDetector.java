import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;

import java.io.IOException;

public class buttonDetector implements IODeviceEventListener{


    //Status:Completed
    //About:Just detects if the button is pressed or not to dispense food.

        private final Pin buttonPin;
        private  final Pin servo;
        // constructor
        buttonDetector(Pin buttonPin, Pin servo) {
            this.buttonPin = buttonPin;
            this.servo = servo;

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




