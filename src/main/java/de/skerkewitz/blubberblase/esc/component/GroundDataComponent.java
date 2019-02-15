package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;

public class GroundDataComponent implements Component {

  public int leftOffset;
  public int rightOffset;
  public int heightOffset;

  public boolean isOnGround = false;

  public GroundDataComponent(int leftOffset, int rightOffset, int heightOffset) {
    this.leftOffset = leftOffset;
    this.rightOffset = rightOffset;
    this.heightOffset = heightOffset;
  }
}
