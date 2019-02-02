package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.enora2d.common.Dice;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.common.Size2i;
import de.skerkewitz.enora2d.core.entity.MoveableEntity;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.Renderer;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The most basic enemy.
 */
public class ZenChan extends MoveableEntity {

  public final static int MAX_LIFETIME_IN_TICKS = 100;
  private static final Logger logger = LogManager.getLogger(ZenChan.class);
  private static final int JUMP_HEIGHT_IN_PIXEL = 44;
  private final Screen.Sprite sprite;
  private int colour = RgbColorPalette.mergeColorCodes(-1, 005, 410, 445);
  private Rect2i sourceRect;
  private Rect2i sourceRect2;

  private Rect2i frameSourceRect;

  private int frameAnimationSpeed = AbstractGame.secondsToTickTime(0.25);

  public ZenChan(int x, int y, int speed, Screen.Sprite sprite) {
    super("ZenChan", x, y, speed, new Rect2i(new Point2i(0, 0), new Size2i(15, 15)));
    this.sprite = sprite;
    sourceRect = new Rect2i(5, 5, 16, 16);
    sourceRect2 = new Rect2i(10 + 16, 5, 16, 16);
    movingDir = Dice.chance(0.5f) ? MoveDirection.Right : MoveDirection.Left;
  }

  @Override
  public void render(Screen screen) {
    Point2i targetPos = new Point2i(posX, posY);
    var flipX = movingDir == MoveDirection.Right;

    Renderer.renderSubImage(sprite.sheet.imageData, frameSourceRect, colour, screen.imageData, targetPos, flipX, false);
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
      if (level.getTileAtPosition(posX - 1, posY).isSolid()) {
        playerMoveDirection = MoveDirection.Right;
      }
    } else if (playerMoveDirection == MoveDirection.Right) {
      if (level.getTileAtPosition(posX + 1 + boundingBox.size.width, posY).isSolid()) {
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

    if (this.posY < 8) {
      this.posY = 8;
    }

    frameSourceRect = (tickTime / frameAnimationSpeed) % 2 == 0 ? sourceRect : sourceRect2;
  }
}
