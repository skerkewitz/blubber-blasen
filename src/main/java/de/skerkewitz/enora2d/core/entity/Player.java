package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.enora2d.core.game.world.World;

public abstract class Player extends MoveableLegacyEntity {

//  private static final int JUMP_HEIGHT_IN_PIXEL = 44;

//  private static final int BUBBLE_SHOOT_DELAY = TimeUtil.secondsToTickTime(0.5);

  /**
   * Last tick time we player spawned a bubble.
   */
//  private int lastBubbleSpawnTime = 0;

  public Player() {
    super("Player", 1);
    this.movingDir = MoveDirection.Right;
  }

  public void tick(World world, int tickTime) {
    super.tick(world, tickTime);

//    int xa = 0;
//    int ya = 0;
//
//    TransformComponent transformComponent = getComponent(TransformComponent.class);
//    InputComponent inputComponent = getComponent(InputComponent.class);
//    if (lastBubbleSpawnTime + BUBBLE_SHOOT_DELAY < tickTime && inputComponent.shoot) {
//      lastBubbleSpawnTime = tickTime;
//      var offsetX = movingDir == MoveDirection.Left ? -8 : +8;
//      Point2f position = new Point2f(transformComponent.position.x + offsetX, transformComponent.position.y - 8);
//      world.addEntity(EntityFactory.spawnBubble(tickTime, position, movingDir, AiBubbleComponent.State.SHOOT));
//      Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (2).wav")).play();
//    }
//
//
//    if (jumpTickRemaining > 0) {
//      jumpTickRemaining -= 1;
//      ya -= 1;
//    } else {
//      boolean isOnGround = getComponent(GroundDataComponent.class).isOnGround;
//      if (isOnGround) {
//        if (inputComponent.jump && isOnGround) {
//          jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
//          Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (6).wav")).play();
//        }
//      } else {
//        ya += 1;
//      }
//    }
//
//    var moveX = 0;
//    var playerMoveDirection = movingDir;
//    if (inputComponent.horizontal < 0) {
//      moveX--;
//      playerMoveDirection = MoveDirection.Left;
//    } else if (inputComponent.horizontal > 0) {
//      moveX++;
//      playerMoveDirection = MoveDirection.Right;
//    }
//
//    moveX = LevelUtils.clipMoveX(moveX, transformComponent.position, getComponent(BoundingBoxComponent.class).getBoundingBox(), world);
//
//    /* Update player position. */
//    Point2f position = getComponent(TransformComponent.class).position;
//    position.x += moveX * speed;
//    position.y += ya * speed;
//
//
//    movingDir = playerMoveDirection;
  }
}
