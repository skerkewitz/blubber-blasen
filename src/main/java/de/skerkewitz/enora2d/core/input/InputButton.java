package de.skerkewitz.enora2d.core.input;

public interface InputButton {
  int getNumTimesPressed();

  boolean isPressed();

  void toggle(boolean isPressed);
}
