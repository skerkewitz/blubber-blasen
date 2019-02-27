package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.LevelScreen;
import de.skerkewitz.blubberblase.esc.component.AiBubbleComponent;
import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.EnemyComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.World;

import static de.skerkewitz.blubberblase.LevelScreen.loadWorldOfLevel;

public class LevelUtils {

  public LevelUtils() {
    /* No instance allowed. */
  }

  public static boolean hasCollided(Entity entity, World world, int xa, int ya) {

    final BoundingBoxComponent boundingBoxComponent = entity.getComponent(BoundingBoxComponent.class);
    final Rect2i boundingBox = boundingBoxComponent.getBoundingBox();
    int xMin = boundingBox.origin.x;
    int xMax = boundingBox.size.width;
    int yMin = boundingBox.origin.y;
    int yMax = boundingBox.size.width;

    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

    if (world.isSolidTile((int) transformComponent.position.x, (int) transformComponent.position.y, xa, ya, xMin, yMin)) {
      return true;
    }

    if (world.isSolidTile((int) transformComponent.position.x, (int) transformComponent.position.y, xa, ya, xMax, yMin)) {
      return true;
    }

    if (world.isSolidTile((int) transformComponent.position.x, (int) transformComponent.position.y, xa, ya, xMin, yMax)) {
      return true;
    }

    return world.isSolidTile((int) transformComponent.position.x, (int) transformComponent.position.y, xa, ya, xMax, yMax);

  }

  public static World loadNextLevel(int tickTime, LevelScreen.GameContext gameContext, GameConfig config) {
    gameContext.currentLevelNum = (gameContext.currentLevelNum + 1) % (LevelScreen.GameContext.MAX_LEVEL + 1);
    if (gameContext.currentLevelNum == 0) {
      gameContext.currentLevelNum += 1;
    }
    return loadWorldOfLevel(tickTime, config, gameContext.currentLevelNum);
  }

  public static boolean isLevelCleared(World world) {
    return world.getEntityContainer()
            .stream()
            .filter(Entity::isAlive).noneMatch(entity -> {
              if (entity.hasComponent(EnemyComponent.class)) {
                return true;
              }

              AiBubbleComponent component = entity.getComponent(AiBubbleComponent.class);
              if (component != null) {
                return component.type == AiBubbleComponent.Type.TRAP;
              }

              return false;
            });
  }

  /**
   * True if the object is standing on ground.
   *
   * @param world
   * @return
   */
  public static boolean isOnGround(Entity entity, World world) {
    return hasCollided(entity, world, 0, +1);
  }
}
