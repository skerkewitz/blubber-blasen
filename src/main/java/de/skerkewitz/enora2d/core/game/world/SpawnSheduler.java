package de.skerkewitz.enora2d.core.game.world;

import de.skerkewitz.enora2d.core.ecs.entity.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpawnSheduler {

  private final List<SpawnInfo> spawnList = new ArrayList<>();

  public void prepareSpawnAtTime(int tickTime, Entity spawnZenChan) {
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

    final int spawnTime;
    final Entity entity;

    SpawnInfo(int spawnTime, Entity entity) {
      this.entity = entity;
      this.spawnTime = spawnTime;
    }
  }
}
