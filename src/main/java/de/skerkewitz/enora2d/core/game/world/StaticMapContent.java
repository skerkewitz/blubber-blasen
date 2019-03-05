package de.skerkewitz.enora2d.core.game.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import de.gierzahn.editor.map.AirflowDirection;
import de.gierzahn.editor.map.EnemyBaseMapLayer;
import de.gierzahn.editor.map.Map;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;
import de.skerkewitz.enora2d.core.game.world.tiles.TileContainer;

import java.util.ArrayList;

public class StaticMapContent {

  public static final int WIDTH = Map.NUM_TILES_HORIZONTAL * Map.TILE_WIDTH;
  public static final int HEIGHT = Map.NUM_TILES_VERTICAL * Map.TILE_HEIGHT;
  private final Sprite tilesetSprite;

  public int tileWidth;
  public int tileHeight;

  private Map map;

  protected StaticMapContent(Map map, Sprite tilesetSprite) {
    this.tilesetSprite = tilesetSprite;
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

  public ArrayList<EnemyBaseMapLayer.Enemy> getEnemySpawnList() {
    return map.enemyMapLayer.toEnemyList();
  }

  public Sprite getTileSprite(int x, int y) {
    if (0 > x || x >= tileWidth || 0 > y || y >= tileHeight) {
      return null;
    }

    // OH boy, need to clean this up ASAP! :D
    final Sprite sprite = tilesetSprite;
    sprite.setSize(8, 8);

    int content = map.staticMapLayer.getAt(x, y);
    if (content == 0) {

      /* Do we have content above? */
      if (y > 0 && map.staticMapLayer.getAt(x, y - 1) != 0) {

        /* Above and on the left .*/
        if (x > 0 && map.staticMapLayer.getAt(x - 1, y) != 0) {
          sprite.setRegion(16, 8, 8, 8);
          sprite.setFlip(false, true);
          return sprite;
        }

        /* Also top left corner? ? */
        if (x > 0 && y > 0 && map.staticMapLayer.getAt(x - 1, y - 1) != 0) {
          sprite.setRegion(24, 8, 8, 8);
          sprite.setFlip(false, true);
          return sprite;
        }

        /* Start shadow. */
        sprite.setRegion(40, 32, 8, 8);
        sprite.setFlip(false, true);
        return sprite;
      }

      /* None above, but on the left? */
      if (x > 0 && map.staticMapLayer.getAt(x - 1, y) != 0) {

        /* Also top left? */
        if (x > 0 && y > 0 && map.staticMapLayer.getAt(x - 1, y - 1) != 0) {
          sprite.setRegion(16, 16, 8, 8);
          sprite.setFlip(false, true);
          return sprite;
        }

        sprite.setRegion(24, 24, 8, 8);
        sprite.setFlip(false, true);
        return sprite;
      }

      /* None above, none on the left but on the top left? */
      if (x > 0 && y > 0 && map.staticMapLayer.getAt(x - 1, y - 1) != 0) {
        sprite.setRegion(24, 32, 8, 8);
        sprite.setFlip(false, true);
        return sprite;
      }

      return null;
    }


    if (x == 0 || x == tileWidth - 2) {
      sprite.setRegion(0, y % 2 * 8, 8, 8);
    } else if (x == 1 || x == tileWidth - 1) {
      sprite.setRegion(8, y % 2 * 8, 8, 8);
    } else {
      sprite.setRegion(3 * 8, 0, 8, 8);
    }

    return tilesetSprite;
  }
}
