package de.skerkewitz.enora2d.core.game.world;

import de.skerkewitz.enora2d.core.ecs.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntitySpawnScheduler {

  public void prepareSpawnAtTime(int frameCount, Entity spawnZenChan) {
    spawnList.add(new SpawnInfo(frameCount, spawnZenChan));
  }

  private final List<SpawnInfo> spawnList = new ArrayList<>();

  public void spawnEntities(int frameCount, World world) {

    Iterator<SpawnInfo> iterator = spawnList.iterator();
    while (iterator.hasNext()) {
      final SpawnInfo spawnInfo = iterator.next();
      if (spawnInfo.spawnFrameCount <= frameCount) {
        world.addEntity(spawnInfo.entity);
        iterator.remove();
      }
    }
  }

  private static class SpawnInfo {

    final int spawnFrameCount;
    final Entity entity;

    SpawnInfo(int spawnFrameCount, Entity entity) {
      this.entity = entity;
      this.spawnFrameCount = spawnFrameCount;
    }
  }
}
