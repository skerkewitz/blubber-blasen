package de.skerkewitz.enora2d.core.game.world;

import de.gierzahn.editor.map.AirflowDirection;
import de.gierzahn.editor.map.Map;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;
import de.skerkewitz.enora2d.core.game.world.tiles.TileContainer;

public class StaticMapContent {

  public static final int WIDTH = Map.NUM_TILES_HORIZONTAL * Map.TILE_WIDTH;
  public static final int HEIGHT = Map.NUM_TILES_VERTICAL * Map.TILE_HEIGHT;

  public int tileWidth;
  public int tileHeight;

  private Map map;

  protected StaticMapContent(Map map) {
    this.tileWidth = WIDTH / 8;
    this.tileHeight = HEIGHT / 8;
    this.map = map;
  }

  /**
   * Get a tile in tile space.
   */
  public Tile getTile(int x, int y) {
    if (0 > x || x >= tileWidth || 0 > y || y >= tileHeight) {
      return TileContainer.VOID;
    }

    int content = map.staticMapLayer.getAt(x, y);
    if (content == 0) {
      return TileContainer.VOID;
    }

    return TileContainer.BB_STONE;
  }

  public boolean isSolidGround(int x, int y) {
    /* Make sure we can not land on top of the level. */
    if (x > 1 && x < tileWidth - 2 && y == 0) {
      return false;
    }

    return getTile(x, y).isSolid();
  }

  public AirflowDirection getAirflowAt(Point2i p) {
    if (0 > p.x || p.x >= tileWidth || 0 > p.y || p.y >= tileHeight) {
      return AirflowDirection.Empty;
    }

    return AirflowDirection.fromCode(map.airflowMapLayer.getAt(p.x, p.y));
  }
}
