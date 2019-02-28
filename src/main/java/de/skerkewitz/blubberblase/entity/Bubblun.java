package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.enora2d.common.Square2i16;
import de.skerkewitz.enora2d.common.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

/**
 * The together with Bob the main protagonist. Bub is the green one.
 */
public interface Bubblun {

  int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(-1, 050, 421, 445);
  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.25);

  Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(0, 7 * 8), Ressources.SpriteSheet),
          new RenderSprite(new Square2i16(16, 7 * 8), Ressources.SpriteSheet)
  );

}
