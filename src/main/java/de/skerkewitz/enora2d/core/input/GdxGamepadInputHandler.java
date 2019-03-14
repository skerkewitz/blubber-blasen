package de.skerkewitz.enora2d.core.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;

public class GdxGamepadInputHandler implements InputHandler {

  private InputButton fireA;
  private InputButton up;
  private InputButton down;
  private InputButton left;
  private InputButton right;

  private Controller controller;

  static class GdxControllerPovInputButton implements InputButton {

    private static final PovDirection[] UP_DIRECTIONS = {PovDirection.north, PovDirection.northWest, PovDirection.northEast};
    private static final PovDirection[] LEFT_DIRECTIONS = {PovDirection.west, PovDirection.northWest, PovDirection.southWest};
    private static final PovDirection[] RIGHT_DIRECTIONS = {PovDirection.east, PovDirection.northEast, PovDirection.southEast};
    private static final PovDirection[] downDirections = {PovDirection.south};
    public final int povCode = 0;
    public final PovDirection[] povDirection;
    private final Controller controller;

    public GdxControllerPovInputButton(Controller controller, PovDirection[] povDirection) {
      this.povDirection = povDirection;
      this.controller = controller;
    }

    @Override
    public boolean isPressed() {
      for (PovDirection direction : povDirection) {
        if (controller.getPov(povCode) == direction) {
          return true;
        }
      }
      return false;
    }
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

  static class GdxControllerStickPovInputButton implements InputButton {

    public final int direction;
    public final int stickCode;
    private final Controller controller;

    public GdxControllerStickPovInputButton(Controller controller, int stickCode, int direction) {
      this.controller = controller;
      this.stickCode = stickCode;
      this.direction = direction;
    }

    @Override
    public boolean isPressed() {
      return direction > 0 ? controller.getAxis(stickCode) > 0.3 : controller.getAxis(stickCode) < -0.3;
    }
  }

  public GdxGamepadInputHandler(Controller controller) {
    this.controller = controller;
    fireA = new GdxInputButton(controller, Xbox.A);
    up = new InputButtonCombiner(new GdxControllerPovInputButton(controller, GdxControllerPovInputButton.UP_DIRECTIONS), new GdxControllerStickPovInputButton(controller, Xbox.L_STICK_VERTICAL_AXIS, -1));
    down = new InputButtonCombiner(new GdxControllerPovInputButton(controller, GdxControllerPovInputButton.downDirections), new GdxControllerStickPovInputButton(controller, Xbox.L_STICK_VERTICAL_AXIS, 1));
    left = new InputButtonCombiner(new GdxControllerPovInputButton(controller, GdxControllerPovInputButton.LEFT_DIRECTIONS), new GdxControllerStickPovInputButton(controller, Xbox.L_STICK_HORIZONTAL_AXIS, -1));
    right = new InputButtonCombiner(new GdxControllerPovInputButton(controller, GdxControllerPovInputButton.RIGHT_DIRECTIONS), new GdxControllerStickPovInputButton(controller, Xbox.L_STICK_HORIZONTAL_AXIS, 1));
  }
}
