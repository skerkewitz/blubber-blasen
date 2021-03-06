package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Square2i16;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.SpriteSource;

/**
 * The together with Bob the main protagonist. Bub is the green one.
 */
public interface Bubblun {

  int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(-1, 050, 421, 445);
  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.25);

  Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new SpriteSource(new Square2i16(0, 7 * 8), Ressources.SpriteSheet),
          new SpriteSource(new Square2i16(16, 7 * 8), Ressources.SpriteSheet)
  );

}
