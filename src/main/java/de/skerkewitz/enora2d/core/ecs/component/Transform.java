package de.skerkewitz.enora2d.core.ecs.component;

import de.skerkewitz.enora2d.common.Point2i;

/**
 * The transform of an entity in world space.
 */
public class Transform implements Component {
  public Point2i position;

  public Transform() {
    this.position = Point2i.ZERO;
  }

  public Transform(Point2i position) {
    this.position = position;
  }
}
