package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.component.AnimationComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.*;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The most basic enemy.
 */
public class ZenChan extends MoveableLegacyEntity {

  public final static int MAX_LIFETIME_IN_TICKS = 100;
  private static final Logger logger = LogManager.getLogger(ZenChan.class);
  private static final int JUMP_HEIGHT_IN_PIXEL = 44;

  public static final int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(-1, 005, 410, 445);
  public static final int FRAME_ANIMATION_SPEED = AbstractGame.secondsToTickTime(0.25);

  public static Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(5, 5), Ressources.SpriteSheet_Enemies),
          new RenderSprite(new Square2i16(10 + 16, 5), Ressources.SpriteSheet_Enemies)
  );

  public ZenChan(int speed) {
    super("ZenChan", speed, new Rect2i(new Point2i(0, 0), new Size2i(15, 15)));
    movingDir = Dice.chance(0.5f) ? MoveDirection.Right : MoveDirection.Left;
  }

  @Override
  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

//    /* Are we dead? */
//    if (this.tickCount > MAX_LIFETIME_IN_TICKS) {
//      this.expired = true;
//      return;
//    }

    super.tick(level, tickTime);

    int xa = 0;
    int ya = 0;

    if (jumpTickRemaining > 0) {
      jumpTickRemaining -= 1;
      ya -= 1;
    } else {
      ya += 2;

      /* There is a 50/50 change every 5s that he will jump if possible. */
      if (tickTime % AbstractGame.TICKTIME_5s == 0 && Dice.chance(0.5f) && isOnGround(level)) {
        jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
      }
    }

    var playerMoveDirection = movingDir;
    if (playerMoveDirection == MoveDirection.Left) {
      TransformComponent transformComponent = getComponent(TransformComponent.class);
      if (level.getTileAtPosition(transformComponent.position.x - 1, transformComponent.position.y).isSolid()) {
        playerMoveDirection = MoveDirection.Right;
      }
    } else if (playerMoveDirection == MoveDirection.Right) {
      TransformComponent transformComponent = getComponent(TransformComponent.class);
      if (level.getTileAtPosition(transformComponent.position.x + 1 + boundingBox.size.width, transformComponent.position.y).isSolid()) {
        playerMoveDirection = MoveDirection.Left;
      }
    }

    if (playerMoveDirection == MoveDirection.Left) {
      xa--;
    } else if (playerMoveDirection == MoveDirection.Right) {
      xa++;
    }

    if (xa != 0 || ya != 0) {
      isMoving = move(level, xa, ya);
    } else {
      isMoving = false;
    }

    logger.debug("Player num steps: " + numSteps);
    movingDir = playerMoveDirection;


    TransformComponent transformComponent = getComponent(TransformComponent.class);
    if (transformComponent.position.y < 8) {
      transformComponent.position.y = 8;
    }

    AnimationComponent animationComponent = getComponent(AnimationComponent.class);
    animationComponent.flipX = movingDir == MoveDirection.Right;
  }
}
