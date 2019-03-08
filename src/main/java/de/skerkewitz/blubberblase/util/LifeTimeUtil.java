package de.skerkewitz.blubberblase.util;

import de.skerkewitz.blubberblase.esc.LifeTimeComponent;
import de.skerkewitz.enora2d.core.game.world.World;

public class LifeTimeUtil {

  private LifeTimeUtil() {
    /* No instance allowed. */
  }

  public static int getAge(int tickTime, World world, LifeTimeComponent lifetimeComponent) {
    return tickTime - lifetimeComponent.spawnTimeFrameCount;
  }
}
