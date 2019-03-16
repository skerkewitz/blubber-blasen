package de.skerkewitz.blubberblase.entity;

import com.badlogic.gdx.math.Interpolation;
import de.skerkewitz.enora2d.core.ecs.Component;

public class RenderSpriteAlphaAnimatorComponent implements Component {

  public final float startAlpha;
  public final float endAlpha;
  public final int startTickTime;
  public final int endTickTime;
  public final Interpolation interpolation;

  public RenderSpriteAlphaAnimatorComponent(float startAlpha, float endAlpha, int startTickTime, int endTickTime, Interpolation interpolation) {
    this.startAlpha = startAlpha;
    this.endAlpha = endAlpha;
    this.startTickTime = startTickTime;
    this.endTickTime = endTickTime;
    this.interpolation = interpolation;
  }
}
