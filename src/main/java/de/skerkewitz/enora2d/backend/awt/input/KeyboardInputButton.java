package de.skerkewitz.enora2d.backend.awt.input;


import de.skerkewitz.enora2d.core.input.InputButton;

/**
 * Encapsulate an input button from any device.
 */
public class KeyboardInputButton implements InputButton {

    private int numTimesPressed = 0;
    private boolean pressed = false;

    @Override
    public int getNumTimesPressed() {
        return numTimesPressed;
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    public void toggle(boolean isPressed) {
        pressed = isPressed;
        if (isPressed) numTimesPressed++;
    }
}
