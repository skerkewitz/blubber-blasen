package de.skerkewitz.enora2d.core.gfx;

import java.util.ArrayList;
import java.util.List;

public class Animation {

  private String name;
  private int animationSpeedInTicks;
  private List<RenderSprite> frames = new ArrayList<>(5);

  public Animation(String name, int animationSpeedInTicks) {
    this.animationSpeedInTicks = animationSpeedInTicks;
    this.name = name;
  }

  public void addFrame(RenderSprite sprite) {
    frames.add(sprite);
  }

  public RenderSprite currentFrame(int currentTime, int animationStartTime) {
    int index = ((currentTime - animationStartTime) / animationSpeedInTicks) % frames.size();
    return frames.get(index);
  }
}
