package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

public interface BonusDiamond {

  int COLOR_PALETTE = RgbColorPalette.mergeRgbColors(RgbColorPalette.NONE, 0x540BFF, 0xFF00FF, 0xffffff);

  /**
   * Will never be remove by the lifecycle common.
   */
  int MAX_LIFETIME_IN_TICKS = TimeUtil.secondsToTickTime(10.0f);

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.1);

//  Animation IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
//          new RenderSprite(new Square2i16(131, 5), Ressources.SpriteSheet_Enemies),
//          new RenderSprite(new Square2i16(152, 5), Ressources.SpriteSheet_Enemies)
//  );
}
