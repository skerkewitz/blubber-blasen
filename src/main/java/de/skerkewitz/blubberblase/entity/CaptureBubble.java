package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.enora2d.common.Square2i16;
import de.skerkewitz.enora2d.common.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

public interface CaptureBubble {

  int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(RgbColorPalette.NONE, RgbColorPalette.BLACK, 533, RgbColorPalette.GREEN);

  int MAX_LIFETIME_IN_TICKS = TimeUtil.secondsToTickTime(7);

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.1);

  Animation IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(131, 5), Ressources.SpriteSheet_Enemies),
          new RenderSprite(new Square2i16(152, 5), Ressources.SpriteSheet_Enemies)
  );
}
