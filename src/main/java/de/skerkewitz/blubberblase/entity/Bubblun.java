package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.component.SpriteComponent;
import de.skerkewitz.enora2d.core.entity.Player;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.input.InputHandler;

/**
 * The together with Bob the main protagonist. Bub is the green one.
 */
public class Bubblun extends Player {

  private final Rect2i sourceRect;
  private final Rect2i sourceRect2;

  private int colour = RgbColorPalette.mergeColorCodes(-1, 050, 421, 445);
  private Rect2i frameSourceRect;

  private int frameAnimationSpeed = AbstractGame.secondsToTickTime(0.25);

  public Bubblun(InputHandler input) {
    super(input);
    sourceRect = new Rect2i(0, 7 * 8, 16, 16);
    sourceRect2 = new Rect2i(16, 7 * 8, 16, 16);
    movingDir = MoveDirection.Right;
  }

  @Override
  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

    frameSourceRect = (tickTime / frameAnimationSpeed) % 2 == 0 ? sourceRect : sourceRect2;
    SpriteComponent spriteComponent = getComponent(SpriteComponent.class);
    spriteComponent.flipX = movingDir == MoveDirection.Left;
    spriteComponent.colorPalette = colour;
    spriteComponent.renderSprite = new RenderSprite(frameSourceRect, Ressources.SpriteSheet);
  }
}
