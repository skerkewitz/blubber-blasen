package de.skerkewitz.enora2d.core.game.world.tiles;

import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

public class TileContainer {

  public static final Tile[] tiles = new Tile[256];
  public static final Tile VOID = new BasicTile(0, 0, 0, RgbColorPalette.mergeColorCodes(000, -1, -1, -1));
  public static final Tile BB_STONE = new BasicSolidTile(4, 3, 0, RgbColorPalette.mergeColorCodes(-1, 502, 555, -1));
  public static final Tile BB_LEVEL2_STONE = new BasicSolidTile(5, 5, 0, RgbColorPalette.mergeRgbColors(-1, 0xAA2926, 0xE1DA61, -1));
  public static final Tile BB_LEVEL3_STONE = new BasicSolidTile(6, 4, 0, RgbColorPalette.mergeRgbColors(-1, 0xAA2926, 0xE1DA61, -1));

  public static void registerTile(Tile tile) {
    if (tiles[tile.id] != null) {
      throw new RuntimeException("Duplicate tile id on " + tile.id);
    }

    tiles[tile.id] = tile;
  }
}
