package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.enora2d.core.game.level.Level;

public abstract class AbstractEntity implements Entity {

  protected int tickCount = 0;
  protected int scale = 1;

  public int posX, posY;
  protected boolean expired;

  protected AbstractEntity() {
    init();
  }

  @Override
  public final void init() {
  }

  @Override
  public void tick(Level level, int tickTime) {
    tickCount += 1;
  }

  @Override
  public final boolean isExpired() {
    return expired;
  }

  @Override
  public final boolean isAlive() {
    return !isExpired();
  }
}
