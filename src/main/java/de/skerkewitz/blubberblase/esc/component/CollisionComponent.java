package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;

public class CollisionComponent implements Component {
  public boolean didCollide = false;

  public void applyCollide(boolean didCollide) {
    if (!didCollide) {
      return;
    }

    this.didCollide = true;
  }
}
