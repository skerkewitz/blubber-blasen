package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Square2i16;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

/**
 * The most basic enemy.
 */
public interface ZenChan {

  int COLOR_PALETTE = RgbColorPalette.mergeRgbColors(-1, 0x145EDC, 0xFC6ECC, 0xC4DEFC);
  int ANGRY_COLOR_PALETTE = RgbColorPalette.mergeRgbColors(-1, 0xB41E7C, 0xFC8274, 0xFCFEFC);
  int ESCAPE_COLOR_PALETTE = RgbColorPalette.mergeRgbColors(-1, 0xB41E7C, 0x5890F0, 0xFCFEFC);
  int THROW_COLOR_PALETTE = RgbColorPalette.mergeRgbColors(-1, 0x145EDC, 0xFC6ECC, 0xC4DEFC);

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.25);
  int ANGRY_FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.2);
  int THROW_FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.1);

  Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new RenderSprite(Ressources.SpriteSheet_Enemies.sheet.rectFor(0, 0), Ressources.SpriteSheet_Enemies),
          new RenderSprite(Ressources.SpriteSheet_Enemies.sheet.rectFor(1, 0), Ressources.SpriteSheet_Enemies)
  );

  Animation THROW = new Animation("throw", THROW_FRAME_ANIMATION_SPEED,
          new RenderSprite(Ressources.SpriteSheet_Enemies.sheet.rectFor(12, 0), Ressources.SpriteSheet_Enemies),
          new RenderSprite(Ressources.SpriteSheet_Enemies.sheet.rectFor(13, 0), Ressources.SpriteSheet_Enemies),
          new RenderSprite(Ressources.SpriteSheet_Enemies.sheet.rectFor(14, 0), Ressources.SpriteSheet_Enemies),
          new RenderSprite(Ressources.SpriteSheet_Enemies.sheet.rectFor(15, 0), Ressources.SpriteSheet_Enemies)
  );

  Animation ANGRY_ANIMATION_IDLE = new Animation("angry_idle", ANGRY_FRAME_ANIMATION_SPEED,
          new RenderSprite(new Square2i16(15 + 32, 5), Ressources.SpriteSheet_Enemies),
          new RenderSprite(new Square2i16(20 + 48, 5), Ressources.SpriteSheet_Enemies)
  );

}
