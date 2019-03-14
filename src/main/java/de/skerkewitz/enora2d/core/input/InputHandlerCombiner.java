package de.skerkewitz.enora2d.core.input;

final public class InputHandlerCombiner implements InputHandler {

  private InputButton fireA;
  private InputButton up;
  private InputButton down;
  private InputButton left;
  private InputButton right;

  public InputHandlerCombiner(InputHandler a, InputHandler b) {
    fireA = new InputButtonCombiner(a.getFireA(), b.getFireA());
    up = new InputButtonCombiner(a.getUp(), b.getUp());
    down = new InputButtonCombiner(a.getDown(), b.getDown());
    left = new InputButtonCombiner(a.getLeft(), b.getLeft());
    right = new InputButtonCombiner(a.getRight(), b.getRight());
  }

  @Override
  public InputButton getFireA() {
    return fireA;
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
}
