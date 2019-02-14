package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.component.AnimationComponent;
import de.skerkewitz.enora2d.common.Square2i16;
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

  public static Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(0, 7 * 8), Ressources.SpriteSheet),
          new RenderSprite(new Square2i16(16, 7 * 8), Ressources.SpriteSheet)
  );

  public Bubblun(InputHandler input) {
    super(input);
    movingDir = MoveDirection.Right;
  }

  @Override
  public void tick(Level level, int tickTime) {
    super.tick(level, tickTime);

    AnimationComponent animationComponent = getComponent(AnimationComponent.class);
    animationComponent.animation = ANIMATION_IDLE;
    animationComponent.currentAnimationStartTimeTick = 0;
    animationComponent.flipX = movingDir == MoveDirection.Left;
  }
}