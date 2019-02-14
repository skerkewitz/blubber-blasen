package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.component.SpriteComponent;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.entity.Player;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.input.InputHandler;

/**
 * The together with Bob the main protagonist. Bub is the green one.
 */
public class Bubblun extends Player {

  public static final int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(-1, 050, 421, 445);
  public static final int FRAME_ANIMATION_SPEED = AbstractGame.secondsToTickTime(0.25);

  private Animation animation = new Animation("idle", FRAME_ANIMATION_SPEED);

  public Bubblun(InputHandler input) {
    super(input);
    movingDir = MoveDirection.Right;

    animation.addFrame(new RenderSprite(new Rect2i(0, 7 * 8, 16, 16), Ressources.SpriteSheet));
    animation.addFrame(new RenderSprite(new Rect2i(16, 7 * 8, 16, 16), Ressources.SpriteSheet));
  }

  @Override
  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

    SpriteComponent spriteComponent = getComponent(SpriteComponent.class);
    spriteComponent.flipX = movingDir == MoveDirection.Left;
    spriteComponent.renderSprite = animation.currentFrame(tickTime, 0);
  }
}
