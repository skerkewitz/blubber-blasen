package de.skerkewitz.enora2d.core.input;

final class InputButtonCombiner implements InputButton {

  private final InputButton a;
  private final InputButton b;

  public InputButtonCombiner(InputButton a, InputButton b) {

    this.a = a;
    this.b = b;
  }

  @Override
  public boolean isPressed() {
    return a.isPressed() || b.isPressed();
  }
}
