package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.blubberblase.entity.Bubble;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.common.Size2i;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.Screen;
import de.skerkewitz.enora2d.core.input.InputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Player extends MoveableEntity {

  private static final Logger logger = LogManager.getLogger(Player.class);

  private static final int JUMP_HEIGHT_IN_PIXEL = 44;

  private static final int BUBBLE_SHOOT_DELAY = AbstractGame.secondsToTickTime(0.5);

  private InputHandler input;
  private int colour = RgbColorPalette.mergeColorCodes(-1, 111, 145, 543);

  /**
   * Last tick time we player spawned a bubble.
   */
  private int lastBubbleSpawnTime = 0;

  public Player(int x, int y, InputHandler input) {
    super("Player", x, y, 1, new Rect2i(new Point2i(0, 0), new Size2i(15, 15)));
    this.input = input;
    this.movingDir = MoveDirection.Right;
  }

  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

    int xa = 0;
    int ya = 0;

    if (lastBubbleSpawnTime + BUBBLE_SHOOT_DELAY < tickTime && input.getFireA().isPressed()) {
      lastBubbleSpawnTime = tickTime;
      level.spawnEntity(new Bubble(this.posX, this.posY, 1));
    }


    if (jumpTickRemaining > 0) {
      jumpTickRemaining -= 1;
      ya -= 1;
    } else {
      ya += 2;

      if (input.getUp().isPressed() && isOnGround(level)) {
        jumpTickRemaining = JUMP_HEIGHT_IN_PIXEL;
      }
    }

    var playerMoveDirection = movingDir;
    if (input.getLeft().isPressed()) {
      xa--;
      playerMoveDirection = MoveDirection.Left;
    }
    if (input.getRight().isPressed()) {
      xa++;
      playerMoveDirection = MoveDirection.Right;
    }

    if (xa != 0 || ya != 0) {
      isMoving = move(level, xa, ya);
    } else {
      isMoving = false;
    }

    logger.debug("Player num steps: " + numSteps);
    movingDir = playerMoveDirection;
  }

  public abstract void render(Screen screen);

//  public boolean hasCollided(Level level, int xa, int ya) {
//    int xMin = 0;
//    int xMax = 15;
//    int yMin = 3;
//    int yMax = 7;
//    for (int x = xMin; x < xMax; x++) {
//      if (level.isSolidTile(this.posX, this.posY, xa, ya, x, yMin)) {
//        return true;
//      }
//    }
//    for (int x = xMin; x < xMax; x++) {
//      if (level.isSolidTile(this.posX, this.posY, xa, ya, x, yMax)) {
//        return true;
//      }
//    }
//    for (int y = yMin; y < yMax; y++) {
//      if (level.isSolidTile(this.posX, this.posY, xa, ya, xMin, y)) {
//        return true;
//      }
//    }
//    for (int y = yMin; y < yMax; y++) {
//      if (level.isSolidTile(this.posX, this.posY, xa, ya, xMax, y)) {
//        return true;
//      }
//    }
//    return false;
//  }
}
