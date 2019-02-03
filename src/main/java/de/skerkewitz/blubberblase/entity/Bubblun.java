package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.entity.Player;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.Renderer;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.Screen;
import de.skerkewitz.enora2d.core.input.InputHandler;

/**
 * The together with Bob the main protagonist. Bub is the green one.
 */
public class Bubblun extends Player {

  private final Screen.Sprite sprite;
  private final Rect2i sourceRect;
  private final Rect2i sourceRect2;

  private int colour = RgbColorPalette.mergeColorCodes(-1, 050, 421, 445);
  private Rect2i frameSourceRect;

  private int frameAnimationSpeed = AbstractGame.secondsToTickTime(0.25);

  public Bubblun(int x, int y, InputHandler input, Screen.Sprite sprite) {
    super(x, y, input);
    this.sprite = sprite;
    sourceRect = new Rect2i(0, 7 * 8, 16, 16);
    sourceRect2 = new Rect2i(16, 7 * 8, 16, 16);
    movingDir = MoveDirection.Right;
  }

  @Override
  public void render(Screen screen) {
    Point2i targetPos = new Point2i(posX, posY);
    var flipX = movingDir == MoveDirection.Left;

    Renderer.renderSubImage(sprite.sheet.imageData, frameSourceRect, colour, screen.imageData, targetPos, flipX, false);
  }

  @Override
  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);
    frameSourceRect = (tickTime / frameAnimationSpeed) % 2 == 0 ? sourceRect : sourceRect2;
  }
}
