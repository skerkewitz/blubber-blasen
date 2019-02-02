package de.skerkewitz.enora2d.core.game.level;

import de.skerkewitz.enora2d.core.entity.Entity;
import de.skerkewitz.enora2d.core.entity.EntityContainer;
import de.skerkewitz.enora2d.core.game.level.tiles.Tile;

public class Level {

  public BackgroundLayer backgroundLayer;

  private final EntityContainer entityContainer;

  public Level() {
    //backgroundLayer = new BackgroundLayer("/levels/water_test_level.png");
    entityContainer = new EntityContainer();
    backgroundLayer = new BackgroundLayer(null);
  }

  public void tick(int tickTime) {

    /* Tick all entities. */
    entityContainer.forEach((Entity e) -> e.tick(this, tickTime));

    entityContainer.purgeExpired();

    /* Tick background layer. */
    backgroundLayer.tick(tickTime);
  }

  public boolean isSolidTile(int px, int py, int xa, int ya, int x, int y) {
    if (backgroundLayer == null) {
      return false;
    }

    Tile lastTile = getTileAtPosition(px + x, py + y);
    Tile newTile = getTileAtPosition(px + x + xa, py + y + ya);
    return !lastTile.equals(newTile) && newTile.isSolid();
  }

  /**
   * Returns the tile at the given pixel position.
   */
  public Tile getTileAtPosition(int x, int y) {
    return backgroundLayer.getTile(x >> 3, y >> 3);
  }


  public void spawnEntity(Entity entity) {
    entityContainer.addEntity(entity);
  }


  public EntityContainer getEntityContainer() {
    return entityContainer;
  }
}
