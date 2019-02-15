package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.GroundDataComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.game.level.tiles.Tile;
import de.skerkewitz.enora2d.core.input.InputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Player extends MoveableLegacyEntity {

  private static final Logger logger = LogManager.getLogger(Player.class);

  private static final int JUMP_HEIGHT_IN_PIXEL = 44;

  private static final int BUBBLE_SHOOT_DELAY = AbstractGame.secondsToTickTime(0.5);

  private InputHandler input;

  /**
   * Last tick time we player spawned a bubble.
   */
  private int lastBubbleSpawnTime = 0;

  public Player(InputHandler input) {
    super("Player", 1);
    this.input = input;
    this.movingDir = MoveDirection.Right;
  }

  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

    int xa = 0;
    int ya = 0;

    TransformComponent transformComponent = getComponent(TransformComponent.class);
    if (lastBubbleSpawnTime + BUBBLE_SHOOT_DELAY < tickTime && input.getFireA().isPressed()) {
      lastBubbleSpawnTime = tickTime;
      level.addEntity(EntityFactory.spawnBubble(tickTime, new Point2i(transformComponent.position)));
    }


    if (jumpTickRemaining > 0) {
      jumpTickRemaining -= 1;
      ya -= 1;
    } else {
      boolean isOnGround = getComponent(GroundDataComponent.class).isOnGround;
      if (isOnGround) {
        if (input.getUp().isPressed() && isOnGround) {
          jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
        }
      } else {
        ya += 1;
      }
    }

    var moveX = 0;
    var playerMoveDirection = movingDir;
    if (input.getLeft().isPressed()) {
      moveX--;
      playerMoveDirection = MoveDirection.Left;
    }
    if (input.getRight().isPressed()) {
      moveX++;
      playerMoveDirection = MoveDirection.Right;
    }

    moveX = clipMoveX(moveX, transformComponent.position, getComponent(BoundingBoxComponent.class).getBoundingBox(), level);

    /* Update player position. */
    Point2i position = getComponent(TransformComponent.class).position;
    position.x += moveX * speed;
    position.y += ya * speed;

//    if (input.getLeft().isPressed()) {
//      xa--;
//      playerMoveDirection = MoveDirection.Left;
//    }
//    if (input.getRight().isPressed()) {
//      xa++;
//      playerMoveDirection = MoveDirection.Right;
//    }
//
//    if (xa != 0 || ya != 0) {
//      isMoving = move(level, xa, ya);
//    } else {
//      isMoving = false;
//    }

    logger.debug("Player num steps: " + numSteps);
    movingDir = playerMoveDirection;
  }

  private int clipMoveX(int moveX, Point2i position, Rect2i boundingBox, Level level) {

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

      oldTile = level.getTileAtPosition(ox, oy);
      newTile = level.getTileAtPosition(ex, ey);
    } else {
      int ox = position.x + (boundingBox.size.width / 2);
      int oy = position.y;

      int ex = position.x + (boundingBox.size.width / 2) + moveX;
      int ey = position.y;

      oldTile = level.getTileAtPosition(ox, oy);
      newTile = level.getTileAtPosition(ex, ey);
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
