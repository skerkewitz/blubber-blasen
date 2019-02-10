package de.skerkewitz.enora2d.core.ecs.component;

public class LifeTimeComponent implements Component {

  public int lifeTimeTC = 0;
  public int spawnTimeTC;

  /**
   * Use -1 for unlimited.
   */
  public int maxLifeTimeTC;

  public LifeTimeComponent(int spawnTimeTC, int maxLiveTimeTC) {
    this.spawnTimeTC = spawnTimeTC;
    this.maxLifeTimeTC = maxLiveTimeTC;
  }
}
