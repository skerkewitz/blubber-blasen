package de.skerkewitz.blubberblase;

import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.game.level.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelSpawner {

  private final List<SpawnInfo> spawnList = new ArrayList<>();

  public void prepareSpawnWithDelay(int tickTime, Entity spawnZenChan) {
    spawnList.add(new SpawnInfo(tickTime, spawnZenChan));
  }

  public void spawnEntities(World world, int tickTime) {

    Iterator<SpawnInfo> iterator = spawnList.iterator();
    while (iterator.hasNext()) {
      final SpawnInfo spawnInfo = iterator.next();
      if (spawnInfo.spawnTime <= tickTime) {
        world.addEntity(spawnInfo.entity);
        iterator.remove();
      }
    }
  }

  private static class SpawnInfo {

    public final int spawnTime;
    public final Entity entity;

    public SpawnInfo(int spawnTime, Entity entity) {
      this.entity = entity;
      this.spawnTime = spawnTime;
    }
  }
}
