package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.SpriteSource;

public interface TrapBubble {

  /**
   * Will never be remove by the lifecycle common.
   */
  int MAX_LIFETIME_IN_TICKS = -1;

  int MAX_LIFETIME_BEFORE_BURST = TimeUtil.secondsToTickTime(5);

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.1);
  int FRAME_ANIMATION_SPEED_RND_OFFSET = TimeUtil.secondsToTickTime(0.05);

  Animation ANIMATION_IDLE = new Animation("idle", FRAME_ANIMATION_SPEED,
          new SpriteSource(new Point2i(0, 0), Ressources.SpriteSheet_BubbleCapture),
          new SpriteSource(new Point2i(1, 0), Ressources.SpriteSheet_BubbleCapture),
          new SpriteSource(new Point2i(2, 0), Ressources.SpriteSheet_BubbleCapture)
  );
}
