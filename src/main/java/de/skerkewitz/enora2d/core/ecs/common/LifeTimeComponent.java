package de.skerkewitz.enora2d.core.ecs.common;

import de.skerkewitz.enora2d.core.ecs.Component;

public class LifeTimeComponent implements Component {

  public final int spawnTimeFrameCount;
  public boolean autoRemove = true;

  /**
   * Use -1 for unlimited.
   */
  public final int maxLifeTimeFrameCount;

  public LifeTimeComponent(int spawnTimeFrameCount, int maxLiveTimeTC) {
    this.spawnTimeFrameCount = spawnTimeFrameCount;
    this.maxLifeTimeFrameCount = maxLiveTimeTC;
  }
}
