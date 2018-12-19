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

public class Player extends MoveableEntity {

  private static final Logger logger = LogManager.getLogger(Player.class);

  private static final int JUMP_HEIGHT_IN_PIXEL = 44;

  private static final int BUBBLE_SHOOT_DELAY = AbstractGame.secondsToTickTime(0.5);

  private InputHandler input;
  private int colour = RgbColorPalette.mergeColorCodes(-1, 111, 145, 543);

  private int jumpTickRemaining = 0;

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
      level.spawnEntity(new Bubble("Bubble", this.posX, this.posY, 1));
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

  public void render(Screen screen) {

    int walkingSpeed = 4;

    int flipTop = (numSteps >> walkingSpeed) & 1;
    int flipBottom = (numSteps >> walkingSpeed) & 1;

    int xTile = 0;
    int yTile = 28;

    if (movingDir == MoveDirection.Up) {
      xTile += 2;
    } else if (movingDir == MoveDirection.Left || movingDir == MoveDirection.Right) {
      xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
      flipTop = (movingDir.ordinal() - 1) % 2;
    }

    int modifier = 8 * scale;
    int xOffset = posX - modifier / 2;
    int yOffset = posY - modifier / 2 - 4;

    screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale);
    screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour, flipTop,
            scale);

    screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour,
            flipBottom, scale);
    screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)
            * 32, colour, flipBottom, scale);


//    if (username != null) {
//      Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10,
//              RgbColorPalette.mergeColorCodes(-1, -1, -1, 555), 1);
//    }
  }

  public boolean hasCollided(Level level, int xa, int ya) {
    int xMin = 0;
    int xMax = 7;
    int yMin = 3;
    int yMax = 7;
    for (int x = xMin; x < xMax; x++) {
      if (level.isSolidTile(this.posX, this.posY, xa, ya, x, yMin)) {
        return true;
      }
    }
    for (int x = xMin; x < xMax; x++) {
      if (level.isSolidTile(this.posX, this.posY, xa, ya, x, yMax)) {
        return true;
      }
    }
    for (int y = yMin; y < yMax; y++) {
      if (level.isSolidTile(this.posX, this.posY, xa, ya, xMin, y)) {
        return true;
      }
    }
    for (int y = yMin; y < yMax; y++) {
      if (level.isSolidTile(this.posX, this.posY, xa, ya, xMax, y)) {
        return true;
      }
    }
    return false;
  }
}
