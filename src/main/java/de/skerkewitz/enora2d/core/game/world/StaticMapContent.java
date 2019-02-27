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
    if (0 > x || x >= tileWidth || 0 > y || y >= tileHeight)
      return TileContainer.VOID;
    return TileContainer.tiles[tiles[x + y * tileWidth]];
  }

  /**
   * Get a tile in pixel space.
   */
  public Tile getTilePixelSpace(int pixelSpaceX, int pixelSpaceY) {
    return getTile(pixelSpaceX >> 8, pixelSpaceY >> 3);
  }

}
