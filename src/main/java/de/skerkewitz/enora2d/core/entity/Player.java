package de.skerkewitz.enora2d.core.entity;

import com.badlogic.gdx.Gdx;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.esc.component.*;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.common.TimeUtil;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;

public abstract class Player extends MoveableLegacyEntity {

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

  public void tick(World world, int tickTime) {
    super.tick(world, tickTime);

    int xa = 0;
    int ya = 0;

    TransformComponent transformComponent = getComponent(TransformComponent.class);
    InputComponent inputComponent = getComponent(InputComponent.class);
    if (lastBubbleSpawnTime + BUBBLE_SHOOT_DELAY < tickTime && inputComponent.shoot) {
      lastBubbleSpawnTime = tickTime;
      var offsetX = movingDir == MoveDirection.Left ? -8 : +8;
      Point2f position = new Point2f(transformComponent.position.x + offsetX, transformComponent.position.y - 8);
      world.addEntity(EntityFactory.spawnBubble(tickTime, position, movingDir, AiBubbleComponent.State.SHOOT));
      Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (2).wav")).play();
    }


    if (jumpTickRemaining > 0) {
      jumpTickRemaining -= 1;
      ya -= 1;
    } else {
      boolean isOnGround = getComponent(GroundDataComponent.class).isOnGround;
      if (isOnGround) {
        if (inputComponent.jump && isOnGround) {
          jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
          Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (6).wav")).play();
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
    Point2f position = getComponent(TransformComponent.class).position;
    position.x += moveX * speed;
    position.y += ya * speed;


    movingDir = playerMoveDirection;
  }
}
