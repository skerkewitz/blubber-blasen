package de.skerkewitz.enora2d.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GdxInputHandler implements InputHandler {

  private InputButton fireA = new GdxInputButton(Input.Keys.SPACE);
  private InputButton up = new GdxInputButton(Input.Keys.UP);
  private InputButton down = new GdxInputButton(Input.Keys.DOWN);
  private InputButton left = new GdxInputButton(Input.Keys.LEFT);
  private InputButton right = new GdxInputButton(Input.Keys.RIGHT);

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

    public GdxInputButton(int key) {
      mappedKey = key;
    }

    @Override
    public boolean isPressed() {
      return Gdx.input.isKeyPressed(mappedKey);
    }

  }
}
