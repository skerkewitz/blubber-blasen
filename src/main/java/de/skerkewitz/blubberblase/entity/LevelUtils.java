package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.game.level.Level;

public class LevelUtils {

  public static boolean hasCollided(Entity entity, Level level, int xa, int ya) {

    final BoundingBoxComponent boundingBoxComponent = entity.getComponent(BoundingBoxComponent.class);
    final Rect2i boundingBox = boundingBoxComponent.getBoundingBox();
    int xMin = boundingBox.origin.x;
    int xMax = boundingBox.size.width;
    int yMin = boundingBox.origin.y;
    int yMax = boundingBox.size.width;

    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

    if (level.isSolidTile(transformComponent.position.x, transformComponent.position.y, xa, ya, xMin, yMin)) {
      return true;
    }

    if (level.isSolidTile(transformComponent.position.x, transformComponent.position.y, xa, ya, xMax, yMin)) {
      return true;
    }

    if (level.isSolidTile(transformComponent.position.x, transformComponent.position.y, xa, ya, xMin, yMax)) {
      return true;
    }

    return level.isSolidTile(transformComponent.position.x, transformComponent.position.y, xa, ya, xMax, yMax);

  }

  /**
   * True if the object is standing on ground.
   *
   * @param level
   * @return
   */
  public static boolean isOnGround(Entity entity, Level level) {
    return hasCollided(entity, level, 0, +1);
  }
}
