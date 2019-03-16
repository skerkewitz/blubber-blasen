package de.skerkewitz.enora2d.core.gfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Animation {

  public enum LoopStyle {
    //    OneShoot,
    CycleForward,
    CycleForwardBackward
  }

  private String name;
  private LoopStyle loopStyle = LoopStyle.CycleForward;
  private int animationSpeedInTicks;
  private List<SpriteSource> frames = new ArrayList<>(5);

  public Animation(String name, int animationSpeedInTicks) {
    this.animationSpeedInTicks = animationSpeedInTicks;
    this.name = name;
  }

  public Animation(String name, int animationSpeedInTicks, SpriteSource... frames) {
    this(name, animationSpeedInTicks);
    this.frames.addAll(Arrays.asList(frames));
  }

  public void addFrame(SpriteSource sprite) {
    frames.add(sprite);
  }

  public LoopStyle getLoopStyle() {
    return loopStyle;
  }

  public void setLoopStyle(LoopStyle loopStyle) {
    this.loopStyle = loopStyle;
  }

  public SpriteSource currentFrame(int currentTime, int animationStartTime, int animationSpeedOffsetInTicks) {

    switch (loopStyle) {
      case CycleForward: {
        int speed = Math.max(1, animationSpeedInTicks + animationSpeedOffsetInTicks);
        int index = ((currentTime - animationStartTime) / speed) % frames.size();
        return frames.get(index);
      }
      case CycleForwardBackward: {
        int speed = Math.max(1, animationSpeedInTicks + animationSpeedOffsetInTicks);
        int numAdditionalFrames = frames.size() - 2;
        int numFrames = frames.size() + numAdditionalFrames;
        int index = ((currentTime - animationStartTime) / speed) % numFrames;
        if (index < frames.size()) {
          return frames.get(index);
        } else {
          return frames.get(numFrames - index);
        }
      }
    }

    throw new IllegalStateException("Unknown loop state: loopStyle");
  }
}
