package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.component.AnimationComponent;
import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.GroundDataComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Dice;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.common.Square2i16;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.entity.Player;
import de.skerkewitz.enora2d.core.game.TimeUtil;
import de.skerkewitz.enora2d.core.game.level.World;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

/**
 * The most basic enemy.
 */
public class ZenChan extends MoveableLegacyEntity {

  public final static int MAX_LIFETIME_IN_TICKS = 100;
  private static final int JUMP_HEIGHT_IN_PIXEL = 44;

  public static final int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(-1, 005, 410, 445);
  public static final int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.25);

  public static Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(5, 5), Ressources.SpriteSheet_Enemies),
          new RenderSprite(new Square2i16(10 + 16, 5), Ressources.SpriteSheet_Enemies)
  );

  public ZenChan(int speed) {
    super("ZenChan", speed);
    movingDir = Dice.chance(0.5f) ? MoveDirection.Right : MoveDirection.Left;
  }

  @Override
  public void tick(World world, int tickTime) {
    super.tick(world, tickTime);

//    /* Are we dead? */
//    if (this.tickCount > MAX_LIFETIME_IN_TICKS) {
//      this.expired = true;
//      return;
//    }

    final TransformComponent transformComponent = getComponent(TransformComponent.class);
    final BoundingBoxComponent boundingBoxComponent = getComponent(BoundingBoxComponent.class);

    int xa = 0;
    int ya = 0;

//    if (jumpTickRemaining > 0) {
//      jumpTickRemaining -= 1;
//      ya -= 1;
//    } else {
//      ya += 2;
//
//      /* There is a 50/50 change every 5s that he will jump if possible. */
//      if (tickTime % TimeUtil.TICKTIME_5s == 0 && Dice.chance(0.5f) && LevelUtils.isOnGround(this, world)) {
//        jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
//      }
//    }

    boolean isOnGround = getComponent(GroundDataComponent.class).isOnGround;
    if (jumpTickRemaining > 0) {
      jumpTickRemaining -= 1;
      ya -= 1;
    } else {
      if (isOnGround) {
        /* There is a 50/50 change every 5s that he will jump if possible. */
        if (tickTime % TimeUtil.TICKTIME_5s == 0 && Dice.chance(0.5f)) {
          jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
        }
      } else {
        ya += 1;
      }
    }

    /* Enemies jump only straight up and fall straight down. */
    var playerMoveDirection = movingDir;
    if (isOnGround) {
      final Rect2i boundingBox = boundingBoxComponent.getBoundingBox();
      if (playerMoveDirection == MoveDirection.Left) {
        if (Player.clipMoveX(-1, transformComponent.position, boundingBox, world) == 0) {
          playerMoveDirection = MoveDirection.Right;
        }
      } else if (playerMoveDirection == MoveDirection.Right) {
        if (Player.clipMoveX(+1, transformComponent.position, boundingBox, world) == 0) {
          playerMoveDirection = MoveDirection.Left;
        }
      }

      if (playerMoveDirection == MoveDirection.Left) {
        xa--;
      } else if (playerMoveDirection == MoveDirection.Right) {
        xa++;
      }

      xa = Player.clipMoveX(xa, transformComponent.position, boundingBox, world);
    }

    /* Update player position. */
    Point2i position = transformComponent.position;
    position.x += xa * speed;
    position.y += ya * speed;


    movingDir = playerMoveDirection;
//
    if (xa != 0 || ya != 0) {
//      isMoving = move(world, xa, ya);
    } else {
      isMoving = false;
    }

    movingDir = playerMoveDirection;

    if (transformComponent.position.y < 8) {
      transformComponent.position.y = 8;
    }

    AnimationComponent animationComponent = getComponent(AnimationComponent.class);
    animationComponent.flipX = movingDir == MoveDirection.Right;
  }
}
