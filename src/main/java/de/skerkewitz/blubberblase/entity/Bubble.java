package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;

public interface Bubble {

  int BURST_MAX_LIFETIME_IN_TICKS = TimeUtil.secondsToTickTime(0.1);

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.1);
  int FRAME_ANIMATION_SPEED_RND_OFFSET = TimeUtil.secondsToTickTime(0.1);

  int MAX_LIFETIME_BEFORE_BURST = TimeUtil.secondsToTickTime(10);

  Animation BUBBLE = new Animation("bubble", FRAME_ANIMATION_SPEED,
          new RenderSprite(Ressources.SpriteSheet_Bubble.sheet.rectFor(0, 0), Ressources.SpriteSheet_Bubble),
          new RenderSprite(Ressources.SpriteSheet_Bubble.sheet.rectFor(1, 0), Ressources.SpriteSheet_Bubble),
          new RenderSprite(Ressources.SpriteSheet_Bubble.sheet.rectFor(2, 0), Ressources.SpriteSheet_Bubble)
  );


  float FLOATING_SPEED = 0.35f;
  float TRAPPED_SPEED = 0.3f;
}
