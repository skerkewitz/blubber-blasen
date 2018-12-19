package de.skerkewitz.enora2d.backend.awt.input;

import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.input.InputButton;
import de.skerkewitz.enora2d.core.input.InputHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputHandler implements KeyListener, InputHandler {

    private InputButton fireA = new KeyboardInputButton();
    private InputButton up = new KeyboardInputButton();
    private InputButton down = new KeyboardInputButton();
    private InputButton left = new KeyboardInputButton();
    private InputButton right = new KeyboardInputButton();

    public KeyboardInputHandler(AbstractGame game) {
        game.addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    public void keyTyped(KeyEvent e) {

    }

    @Override
    public InputButton getUp() {
        return up;
    }

    @Override
    public InputButton getDown() {
        return down;
    }

    @Override
    public InputButton getLeft() {
        return left;
    }

    @Override
    public InputButton getRight() {
        return right;
    }

    @Override
    public InputButton getFireA() {
        return fireA;
    }

    private void toggleKey(int keyCode, boolean isPressed) {

        if (keyCode == KeyEvent.VK_SPACE) {
            fireA.toggle(isPressed);
        }

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            up.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            down.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            left.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            right.toggle(isPressed);
        }
    }
}
