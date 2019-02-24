package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.GroundDataComponent;
import de.skerkewitz.blubberblase.esc.component.InputComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.game.TimeUtil;
import de.skerkewitz.enora2d.core.game.level.World;
import de.skerkewitz.enora2d.core.game.level.tiles.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Player extends MoveableLegacyEntity {

  private static final Logger logger = LogManager.getLogger(Player.class);

  private static final int JUMP_HEIGHT_IN_PIXEL = 44;

  private static final int BUBBLE_SHOOT_DELAY = TimeUtil.secondsToTickTime(0.5);

  /**
   * Last tick time we player spawned a bubble.
   */
  private int lastBubbleSpawnTime = 0;

  public Player() {
    super("Player", 1);
    this.movingDir = MoveDirection.Right;
  }

  public void tick(World world, int tickTime) {
    super.tick(world, tickTime);

    int xa = 0;
    int ya = 0;

    TransformComponent transformComponent = getComponent(TransformComponent.class);
    InputComponent inputComponent = getComponent(InputComponent.class);
    if (lastBubbleSpawnTime + BUBBLE_SHOOT_DELAY < tickTime && inputComponent.shoot) {
      lastBubbleSpawnTime = tickTime;
      var offsetX = movingDir == MoveDirection.Left ? -8 : +8;
      Point2i position = new Point2i(transformComponent.position.x + offsetX, transformComponent.position.y - 8);
      world.addEntity(EntityFactory.spawnBubble(tickTime, position, movingDir));
    }


    if (jumpTickRemaining > 0) {
      jumpTickRemaining -= 1;
      ya -= 1;
    } else {
      boolean isOnGround = getComponent(GroundDataComponent.class).isOnGround;
      if (isOnGround) {
        if (inputComponent.jump && isOnGround) {
          jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
        }
      } else {
        ya += 1;
      }
    }

    var moveX = 0;
    var playerMoveDirection = movingDir;
    if (inputComponent.horizontal < 0) {
      moveX--;
      playerMoveDirection = MoveDirection.Left;
    } else if (inputComponent.horizontal > 0) {
      moveX++;
      playerMoveDirection = MoveDirection.Right;
    }

    moveX = clipMoveX(moveX, transformComponent.position, getComponent(BoundingBoxComponent.class).getBoundingBox(), world);

    /* Update player position. */
    Point2i position = getComponent(TransformComponent.class).position;
    position.x += moveX * speed;
    position.y += ya * speed;


    movingDir = playerMoveDirection;
  }

  public static int clipMoveX(int moveX, Point2i position, Rect2i boundingBox, World world) {

    /* no horizontal movement. */
    if (moveX == 0) {
      return 0;
    }

    Tile oldTile;
    Tile newTile;
    if (moveX < 0) {

      int ox = position.x - (boundingBox.size.width / 2);
      int oy = position.y;

      int ex = position.x - (boundingBox.size.width / 2) + moveX;
      int ey = position.y;

      oldTile = world.getTileAtPosition(ox, oy);
      newTile = world.getTileAtPosition(ex, ey);
    } else {
      int ox = position.x + (boundingBox.size.width / 2);
      int oy = position.y;

      int ex = position.x + (boundingBox.size.width / 2) + moveX;
      int ey = position.y;

      oldTile = world.getTileAtPosition(ox, oy);
      newTile = world.getTileAtPosition(ex, ey);
    }

    if (oldTile.isSolid()) {
      return moveX;
    }

    if (!newTile.isSolid()) {
      return moveX;
    }

    return 0;
  }
}
