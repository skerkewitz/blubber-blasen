package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.enora2d.common.Square2i16;
import de.skerkewitz.enora2d.common.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

/**
 * The most basic enemy.
 */
public interface ZenChan {

  int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(-1, 005, 410, 445);
  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.25);

  Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(5, 5), Ressources.SpriteSheet_Enemies),
          new RenderSprite(new Square2i16(10 + 16, 5), Ressources.SpriteSheet_Enemies)
  );
}
