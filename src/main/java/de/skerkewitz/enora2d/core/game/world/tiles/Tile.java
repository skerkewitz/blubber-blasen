package de.skerkewitz.enora2d.core.game.world.tiles;

public abstract class Tile {

  protected boolean solid;

  public Tile(boolean isSolid) {
    this.solid = isSolid;
  }

  public boolean isSolid() {
    return solid;
  }
}
