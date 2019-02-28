package de.skerkewitz.enora2d.core.game.world;

import de.skerkewitz.enora2d.core.game.world.tiles.Tile;
import de.skerkewitz.enora2d.core.game.world.tiles.TileContainer;

public class StaticMapContent {

  public static final int WIDTH = 256;
  public static final int HEIGHT = 200;

  public int tileWidth;
  public int tileHeight;
  private byte[] tiles;

  protected StaticMapContent(byte[] tiles) {
    this.tileWidth = WIDTH / 8;
    this.tileHeight = HEIGHT / 8;
    this.tiles = tiles;
//    this.generateLevel();
  }

  /**
   * Get a tile in tile space.
   */
  public Tile getTile(int x, int y) {
    if (0 > x || x >= tileWidth || 0 > y || y >= tileHeight) {
      return TileContainer.VOID;
    }

    return TileContainer.tiles[tiles[x + y * tileWidth]];
  }

  public boolean isSolidGround(int x, int y) {
    /* Make sure we can not land on top of the level. */
    if (x > 1 && x < tileWidth - 2 && y == 0) {
      return false;
    }

    return getTile(x, y).isSolid();
  }
}
