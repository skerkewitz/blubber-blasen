package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.enora2d.core.ecs.LegacyEntity;
import de.skerkewitz.enora2d.core.ecs.entity.DefaultEntity;
import de.skerkewitz.enora2d.core.game.level.World;

public abstract class AbstractLegacyEntity extends DefaultEntity implements LegacyEntity {

  protected int tickCount = 0;


  @Override
  public void tick(World world, int tickTime) {
    tickCount += 1;
  }


}
