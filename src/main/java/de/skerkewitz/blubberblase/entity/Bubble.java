package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.common.Size2i;
import de.skerkewitz.enora2d.core.ecs.component.Transform;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.game.level.Level;

public class Bubble extends MoveableLegacyEntity {

  public final static int MAX_LIFETIME_IN_TICKS = 100;

  Bubble(int speed) {
    super("Bubble", speed, new Rect2i(new Point2i(0, 0), new Size2i(15, 15)));
  }

  @Override
  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

    /* Are we dead? */
    if (this.tickCount > MAX_LIFETIME_IN_TICKS) {
      this.expired = true;
      return;
    }

    if (numSteps > 8 * 6 && this.movingDir != MoveDirection.Up) {
      if (move(level, 0, -1)) {
        this.movingDir = MoveDirection.Up;
        numSteps = 0;
      }
    }

    switch (this.movingDir) {
      case Up:
        if (!move(level, 0, -1)) {
          this.movingDir = MoveDirection.Left;
        }
        break;
      case Left:
        if (!move(level, -1, 0)) {
          this.movingDir = MoveDirection.Right;
        }
        break;
      case Right:
        if (!move(level, +1, 0)) {
          this.movingDir = MoveDirection.Up;
        }
        break;
      case Down:
        if (!move(level, 0, 1)) {
          this.movingDir = MoveDirection.Left;
        }
        break;
    }

    Transform transform = getComponent(Transform.class);
    if (transform.position.y < 8) {
      transform.position.y = 8;
    }
  }
}
