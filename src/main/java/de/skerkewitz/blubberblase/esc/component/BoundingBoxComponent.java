package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.Component;

public class BoundingBoxComponent implements Component {
  private final Rect2i boundingBox;

  public BoundingBoxComponent(Rect2i boundingBox) {
    this.boundingBox = boundingBox;
  }

  public Rect2i getBoundingBox() {
    return boundingBox;
  }
}
