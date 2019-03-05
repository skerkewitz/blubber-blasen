package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.Component;

public class TargetMoveComponent implements Component {

  public final Point2i position;
  public final float speed;


  public TargetMoveComponent(Point2i position, float speed) {
    this.position = position;
    this.speed = speed;
  }
}
