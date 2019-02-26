package de.skerkewitz.enora2d.core.game.world.tiles;

public abstract class Tile {

  protected byte id;
  protected boolean solid;

  public Tile(int id, boolean isSolid) {
    this.id = (byte) id;
    this.solid = isSolid;

    TileContainer.registerTile(this);
  }


  public byte getId() {
    return id;
  }

  public boolean isSolid() {
    return solid;
  }

}
