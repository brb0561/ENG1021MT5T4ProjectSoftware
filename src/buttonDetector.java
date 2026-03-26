import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;

import java.io.IOException;

public class buttonDetector implements IODeviceEventListener{


    //Status:Completed

        private final Pin buttonPin;
        private  final Pin waterPumpPin;
        // constructor
        buttonDetector(Pin buttonPin, Pin waterPump) {
            this.buttonPin = buttonPin;
            this.waterPumpPin = waterPump;

        }

        @Override
        public void onPinChange(IOEvent event) {
            // Return right away if the even isn't from the Button.
            if (event.getPin().getIndex() != buttonPin.getIndex()) {
                return;
            }
            try {//ends the water pump from the press of a button.
                this.waterPumpPin.setValue(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
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




