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
  private final StaticMapContentLoader.SpriteTileset tilesetSprite;

  public int tileWidth;
  public int tileHeight;

  private Map map;

  protected StaticMapContent(Map map, StaticMapContentLoader.SpriteTileset tilesetSprite) {
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

    int content = map.staticMapLayer.getAt(x, y);
    if (content == 0) {

      /* Do we have content above? */
      if (y > 0 && map.staticMapLayer.getAt(x, y - 1) != 0) {

        /* Above and on the left .*/
        if (x > 0 && map.staticMapLayer.getAt(x - 1, y) != 0) {
          return tilesetSprite.shadow_innerCorner;
        }

        /* Also top left corner? ? */
        if (x > 0 && y > 0 && map.staticMapLayer.getAt(x - 1, y - 1) != 0) {
          return tilesetSprite.shadow_straightHorizontal;
        }

        /* Start shadow. */
        return tilesetSprite.shadow_startHorizontal;
      }

      /* None above, but on the left? */
      if (x > 0 && map.staticMapLayer.getAt(x - 1, y) != 0) {

        /* Also top left? */
        if (x > 0 && y > 0 && map.staticMapLayer.getAt(x - 1, y - 1) != 0) {
          return tilesetSprite.shadow_straightVertical;
        }

        return tilesetSprite.shadow_startVertical;
      }

      /* None above, none on the left but on the top left? */
      if (x > 0 && y > 0 && map.staticMapLayer.getAt(x - 1, y - 1) != 0) {
        return tilesetSprite.shadow_outerCorner;
      }

      return null;
    }


    if (x == 0 || x == tileWidth - 2) {
      return y % 2 == 0 ? tilesetSprite.fourBlock_topLeft : tilesetSprite.fourBlock_bottomLeft;
    } else if (x == 1 || x == tileWidth - 1) {
      return y % 2 == 0 ? tilesetSprite.fourBlock_topright : tilesetSprite.fourBlock_bottomRight;
    }

    return tilesetSprite.defaultBlock;
  }
}
