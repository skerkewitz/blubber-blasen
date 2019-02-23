package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;
import de.skerkewitz.enora2d.core.input.InputHandler;

public class InputComponent implements Component {

  public InputHandler inputHandler;

  public int horizontal = 0;
  public int vertical = 0;

  public boolean shoot = false;
  public boolean jump = false;

  public InputComponent(InputHandler inputHandler) {
    this.inputHandler = inputHandler;
  }
}
