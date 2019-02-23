package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.component.Component;

/**
 * The transform of an entity in world space.
 */
public class TransformComponent implements Component {
  public Point2i position;

  public TransformComponent() {
    this.position = Point2i.ZERO;
  }

  public TransformComponent(Point2i position) {
    this.position = position;
  }


}
