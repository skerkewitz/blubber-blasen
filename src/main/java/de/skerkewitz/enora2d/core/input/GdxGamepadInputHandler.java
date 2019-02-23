package de.skerkewitz.enora2d.core.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Xbox;

public class GdxGamepadInputHandler implements InputHandler {

  private InputButton fireA;
  private InputButton up;
  private InputButton down;
  private InputButton left;
  private InputButton right;

  private Controller controller;

  public GdxGamepadInputHandler(Controller controller) {
    this.controller = controller;
    fireA = new GdxInputButton(controller, Xbox.A);
    up = new GdxInputButton(controller, Xbox.B);
    down = new GdxInputButton(controller, Xbox.DPAD_DOWN);
    left = new GdxInputButton(controller, Xbox.DPAD_LEFT);
    right = new GdxInputButton(controller, Xbox.DPAD_RIGHT);
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

  static class GdxInputButton implements InputButton {

    public final int mappedKey;
    private final Controller controller;

    public GdxInputButton(Controller controller, int key) {
      this.controller = controller;
      mappedKey = key;
    }

    @Override
    public boolean isPressed() {
      return controller.getButton(mappedKey);
    }
  }
}
