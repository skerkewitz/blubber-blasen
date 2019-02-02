package de.skerkewitz.enora2d.core.game.level.tiles;

import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

public class TileContainer {

  public static final Tile[] tiles = new Tile[256];
  public static final Tile VOID = new BasicTile(0, 0, 0, RgbColorPalette.mergeColorCodes(000, -1, -1, -1), 0xFF000000);
  public static final Tile STONE = new BasicSolidTile(1, 1, 0, RgbColorPalette.mergeColorCodes(-1, 5, -1, -1), 0xFF555555);
  public static final Tile GRASS = new BasicTile(2, 2, 0, RgbColorPalette.mergeColorCodes(-1, 131, 141, -1), 0xFF00FF00);

  public static final Tile BB_STONE = new BasicSolidTile(4, 3, 0, RgbColorPalette.mergeColorCodes(-1, 502, 544, -1), 0xFF00FF00);
  public static final Tile WATER = new AnimatedTile(3, new int[][]{{0, 5}, {1, 5}, {2, 5}, {1, 5}},
          RgbColorPalette.mergeColorCodes(-1, 004, 115, -1), 0xFF0000FF, 1000);


  public static void registerTile(Tile tile) {
    if (tiles[tile.id] != null) {
      throw new RuntimeException("Duplicate tile id on " + tile.id);
    }

    tiles[tile.id] = tile;
  }
}
