package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.Component;

public class LifeTimeComponent implements Component {

  public final int lifeTimeFrameCount = 0;
  public final int spawnTimeFrameCount;

  /**
   * Use -1 for unlimited.
   */
  public final int maxLifeTimeFrameCount;

  public LifeTimeComponent(int spawnTimeFrameCount, int maxLiveTimeTC) {
    this.spawnTimeFrameCount = spawnTimeFrameCount;
    this.maxLifeTimeFrameCount = maxLiveTimeTC;
  }
}
