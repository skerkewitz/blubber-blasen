package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Square2i16;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

public interface TrapBubble {

  int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(RgbColorPalette.NONE, RgbColorPalette.BLACK, 533, RgbColorPalette.GREEN);

  /**
   * Will never be remove by the lifecycle system.
   */
  int MAX_LIFETIME_IN_TICKS = -1;

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.1);

  Animation IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(131, 5), Ressources.SpriteSheet_Enemies),
          new RenderSprite(new Square2i16(152, 5), Ressources.SpriteSheet_Enemies)
  );
}
