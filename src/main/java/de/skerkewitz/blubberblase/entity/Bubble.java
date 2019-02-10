package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.enora2d.core.ecs.LegacyEntity;
import de.skerkewitz.enora2d.core.ecs.entity.DefaultEntity;
import de.skerkewitz.enora2d.core.game.level.Level;

public class Bubble extends DefaultEntity implements LegacyEntity {

  public final static int MAX_LIFETIME_IN_TICKS = 100;

  Bubble(int speed) {
//    super("Bubble", speed, new Rect2i(new Point2i(0, 0), new Size2i(15, 15)));
//    movingDir = MoveDirection.Right;
  }


  @Override
  public void tick(Level level, int tickTime) {
    //super.tick(level, tickTime);

//    /* Are we dead? */
//    tickCount++;
//    if (this.tickCount > MAX_LIFETIME_IN_TICKS) {
//      this.expired = true;
//      return;
//    }
//
//    TransformComponent transformComponent = getComponent(TransformComponent.class);
//    if (transformComponent.position.y < 8) {
//      transformComponent.position.y = 8;
//    }
  }
}
