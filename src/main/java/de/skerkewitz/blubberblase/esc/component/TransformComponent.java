package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.Component;

/**
 * The transform of an entity in world space.
 */
public class TransformComponent implements Component {
  public Point2f position;

  public TransformComponent() {
    this.position = Point2f.ZERO;
  }

  public TransformComponent(Point2f position) {
    this.position = position;
  }


}
