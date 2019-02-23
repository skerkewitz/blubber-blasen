package de.skerkewitz.enora2d.core.game.level.tiles;

public abstract class Tile {

  protected byte id;
  protected boolean solid;
  protected boolean emitter;
  private int levelColour;

  public Tile(int id, boolean isSolid, boolean isEmitter, int levelColour) {
    this.id = (byte) id;

    this.solid = isSolid;
    this.emitter = isEmitter;
    this.levelColour = levelColour;

    TileContainer.registerTile(this);
  }


  public byte getId() {
    return id;
  }

  public boolean isSolid() {
    return solid;
  }

  public boolean isEmitter() {
    return emitter;
  }

  public int getLevelColour() {
    return levelColour;
  }
}
