package de.skerkewitz.enora2d.core.game.level.tiles;

public class BasicTile extends Tile {

  protected int tileId;
  protected int tileColour;

  public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
    super(id, false, false, levelColour);
    this.tileId = x + y * 32;
    this.tileColour = tileColour;
  }

  public int getTileColour() {
    return tileColour;
  }


  public int getTileId() {
    return tileId;
  }
}
