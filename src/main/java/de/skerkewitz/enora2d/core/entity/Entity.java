package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.Screen;

public interface Entity {

  boolean isExpired();

  boolean isAlive();

  class ObjectAttributes {

    int size = 1; // 1 means 8x8, 2 means 16x16
    //int tileIndex;

  }

  // Size (8x8, 16x16), pos, prio, flipv, fliph, first tile, color palette

  void init();

  void tick(Level level, int tickTime);

  void render(Screen screen);
}
