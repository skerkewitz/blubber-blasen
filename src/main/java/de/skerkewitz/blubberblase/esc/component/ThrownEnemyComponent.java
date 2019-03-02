package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.Component;

public class ThrownEnemyComponent implements Component {

  public float speed = 2.5f;

  public Point2i moveVector = new Point2i(1, -1);
}
