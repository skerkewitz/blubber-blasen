package de.skerkewitz.enora2d.core.ecs;

import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.game.level.Level;

/**
 * Any entity in the game.
 */
public interface LegacyEntity extends Entity {

  class ObjectAttributes {

    int size = 1; // 1 means 8x8, 2 means 16x16
    //int tileIndex;

  }

  // Size (8x8, 16x16), pos, prio, flipv, fliph, first tile, color palette

  void tick(Level level, int tickTime);
}
