package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

public interface Bubble {

  int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(RgbColorPalette.NONE, RgbColorPalette.GREEN, RgbColorPalette.NONE, RgbColorPalette.WHITE);

  int MAX_LIFETIME_IN_TICKS = 400;

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.05);
  int FRAME_ANIMATION_RND_SPEED = TimeUtil.secondsToTickTime(0.10);

  int MAX_LIFETIME_BEFORE_BURST = TimeUtil.secondsToTickTime(5);

  Animation BUBBLE = new Animation("bubble", FRAME_ANIMATION_SPEED + FRAME_ANIMATION_RND_SPEED,
          new RenderSprite(Ressources.SpriteSheet_Bubble.sheet.rectFor(0, 0), Ressources.SpriteSheet_Bubble),
          new RenderSprite(Ressources.SpriteSheet_Bubble.sheet.rectFor(1, 0), Ressources.SpriteSheet_Bubble),
          new RenderSprite(Ressources.SpriteSheet_Bubble.sheet.rectFor(2, 0), Ressources.SpriteSheet_Bubble)
  );


  float FLOATING_SPEED = 0.35f;
  float TRAPPED_SPEED = 0.3f;
}
