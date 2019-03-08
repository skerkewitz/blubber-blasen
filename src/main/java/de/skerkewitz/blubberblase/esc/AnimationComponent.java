package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.enora2d.core.ecs.Component;
import de.skerkewitz.enora2d.core.gfx.Animation;

public class AnimationComponent implements Component {

  public int currentAnimationStartTimeTick;
  public Animation animation;
  public boolean flipX;
  public final int animationSpeedOffsetInTicks;

  public AnimationComponent(int tickTime, Animation animation, boolean flipX, int animationSpeedOffsetInTicks) {
    this.currentAnimationStartTimeTick = tickTime;
    this.animation = animation;
    this.flipX = flipX;
    this.animationSpeedOffsetInTicks = animationSpeedOffsetInTicks;
  }
}
