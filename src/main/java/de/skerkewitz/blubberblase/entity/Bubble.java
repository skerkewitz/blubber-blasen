package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.common.Size2i;
import de.skerkewitz.enora2d.core.entity.MoveableEntity;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.Screen;

public class Bubble extends MoveableEntity {

  private int colour = RgbColorPalette.mergeColorCodes(-1, 050, -1, 555);

  public final static int MAX_LIFETIME_IN_TICKS = 100;

  public Bubble(int x, int y, int speed) {
    super("Bubble", x, y, speed, new Rect2i(new Point2i(0, 0), new Size2i(15, 15)));
  }

  @Override
  public void render(Screen screen) {

    int xTile = 0;
    int yTile = 25;

    int modifier = 8 * scale;
    int xOffset = posX;
    int yOffset = posY;

    screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0, scale);
    screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32, colour, 0, scale);
    screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32, colour, 0, scale);
    screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, 0, scale);
  }

  @Override
  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

    /* Are we dead? */
    if (this.tickCount > MAX_LIFETIME_IN_TICKS) {
      this.expired = true;
      return;
    }

    if (numSteps > 8 * 6 && this.movingDir != MoveDirection.Up) {
      if (move(level, 0, -1)) {
        this.movingDir = MoveDirection.Up;
        numSteps = 0;
      }
    }

    switch (this.movingDir) {
      case Up:
        if (!move(level, 0, -1)) {
          this.movingDir = MoveDirection.Left;
        }
        break;
      case Left:
        if (!move(level, -1, 0)) {
          this.movingDir = MoveDirection.Right;
        }
        break;
      case Right:
        if (!move(level, +1, 0)) {
          this.movingDir = MoveDirection.Up;
        }
        break;
      case Down:
        if (!move(level, 0, 1)) {
          this.movingDir = MoveDirection.Left;
        }
        break;
    }

    if (this.posY < 8) {
      this.posY = 8;
    }
  }
}
