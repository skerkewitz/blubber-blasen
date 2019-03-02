package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.EnemyComponent;
import de.skerkewitz.blubberblase.esc.component.StateBaseBubbleComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.StaticMapContent;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;

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

  public static World loadNextLevel(int tickTime, GameContext gameContext, GameConfig config) {

    gameContext.currentLevelNum += 1;
    gameContext.clampLevelNum();

    return loadWorldOfLevel(tickTime, config, gameContext.currentLevelNum);
  }

  public static boolean isLevelCleared(World world) {
    return world.getEntityContainer()
            .stream()
            .filter(Entity::isAlive)
            .noneMatch(entity -> {
              if (entity.hasComponent(EnemyComponent.class)) {
                return true;
              }

              StateBaseBubbleComponent component = entity.getComponent(StateBaseBubbleComponent.class);
              if (component != null) {
                return component.type == StateBaseBubbleComponent.Type.TRAP;
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

  public static boolean isGapInFront(int moveX, Point2f position, Rect2i boundingBox, World world) {

    /* If we don't move left or right then there is no gap. */
    if (moveX == 0) {
      return false;
    }

    final Tile tile;
    if (moveX < 0) {
      float ex = position.x - (boundingBox.size.width / 2) + moveX;
      float ey = position.y + 1;

      tile = world.getTileAtPosition((int) ex, (int) ey);
    } else {
      float ex = position.x + (boundingBox.size.width / 2) + moveX;
      float ey = position.y + 1;

      tile = world.getTileAtPosition((int) ex, (int) ey);
    }

    /* if tile is not solid then there is a gap. */
    return !tile.isSolid();
  }


  public static int clipMoveX(int moveX, Point2f position, Rect2i boundingBox, World world) {

    /* no horizontal movement. */
    if (moveX == 0) {
      return 0;
    }

    Tile oldTile;
    Tile newTile;
    if (moveX < 0) {

      float ox = position.x - (boundingBox.size.width / 2);
      float oy = position.y;

      float ex = position.x - (boundingBox.size.width / 2) + moveX;
      float ey = position.y;

      oldTile = world.getTileAtPosition((int) ox, (int) oy);
      newTile = world.getTileAtPosition((int) ex, (int) ey);
    } else {
      float ox = position.x + (boundingBox.size.width / 2);
      float oy = position.y;

      float ex = position.x + (boundingBox.size.width / 2) + moveX;
      float ey = position.y;

      oldTile = world.getTileAtPosition((int) ox, (int) oy);
      newTile = world.getTileAtPosition((int) ex, (int) ey);
    }

    if (oldTile.isSolid()) {
      return moveX;
    }

    if (!newTile.isSolid()) {
      return moveX;
    }

    return 0;
  }

  public static boolean canGapJump(int horizontalMoveVector, Point2f position, Rect2i boundingBox, World world) {

    /* If we don't move left or right then there is no gap. */
    if (horizontalMoveVector == 0) {
      return false;
    }

    final int tileX;
    final int tileY;

    if (horizontalMoveVector < 0) {
      tileX = (int) (position.x - (boundingBox.size.width / 2) + horizontalMoveVector);
      tileY = (int) (position.y + 1);
    } else {
      tileX = (int) (position.x + (boundingBox.size.width / 2) + horizontalMoveVector);
      tileY = (int) (position.y + 1);
    }

    /* if tile is not solid then there is a gap. */
    return world.canGapJumpAtPosition(tileX, tileY, horizontalMoveVector);
  }

  public static void clipPositionToLevelBounds(Point2f position, Rect2i boundingBox) {

    var minX = (int) (position.x - (boundingBox.size.width / 2));
    var maxX = (int) (position.x + (boundingBox.size.width / 2));

    if (minX < 16) {
      position.x += (16 - minX);
    } else if (maxX > 239) {
      position.x -= (maxX - 239);
    }

    /* Wrap around if entity did fall through. */
    if (position.y > StaticMapContent.HEIGHT + 16) {
      position.y = -16;
    }
  }

  public static boolean canJumpUp(Point2f position, World world) {

    final int tileX = (int) (position.x);
    final int tileY = (int) (position.y + 1);

    /* if tile is not solid then there is a gap. */
    return world.canJumpUp(tileX, tileY);
  }

  public static boolean checkGround(Point2i position, World world) {
    final boolean lastTile = world.isSolidAtPosition(position.x, position.y);
    final boolean newTile = world.isSolidAtPosition(position.x, position.y + 1);
    return !lastTile && newTile;
  }
}
