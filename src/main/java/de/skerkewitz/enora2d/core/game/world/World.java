package de.skerkewitz.enora2d.core.game.world;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.EntityContainer;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;

public abstract class World {

  public final int numHorizontalTiles = StaticMapContent.WIDTH / 8;
  public final int numVerticalTiles = StaticMapContent.HEIGHT / 8;
  private final EntitySpawnScheduler spawnSheduler = new EntitySpawnScheduler();

  protected final EntityContainer entityContainer;
  public StaticMapContent staticMapContent;

  private Entity playerEntity;

  public World(StaticMapContent staticMapContent) {
    entityContainer = new EntityContainer();
    this.staticMapContent = staticMapContent;
  }

  public void tick(int tickTime, GameContext context) {
    spawnSheduler.spawnEntities(tickTime, this);
  }

  public boolean isSolidTile(int px, int py, int xa, int ya, int x, int y) {
    if (staticMapContent == null) {
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
    return staticMapContent.getTile(x >> 3, y >> 3);
  }


  public void addEntity(Entity entity) {
    entityContainer.addEntity(entity);
  }


  public EntityContainer getEntityContainer() {
    return entityContainer;
  }


  public void prepareSpawnAtTime(int frameCount, Entity entity) {
    this.spawnSheduler.prepareSpawnAtTime(frameCount, entity);
  }

  public Entity getPlayerEntity() {
    return playerEntity;
  }

  private void setPlayerEntity(Entity playerEntity) {
    this.playerEntity = playerEntity;
  }

  public void addPlayer(Entity playerEntity) {
    addEntity(playerEntity);
    setPlayerEntity(playerEntity);
  }

  public boolean canGapJumpAtPosition(int x, int y, int moveDir) {

    int tx = x >> 8;
    int ty = y >> 8;

    /* Maximum Gap distance is 3. Find a solid tile so we can jump. Very naive approach*/
    for (var i = 0; i < 2; i++) {
      if (staticMapContent.getTile(tx + (moveDir * i), ty).isSolid()) {
        return true;
      }
    }

    return false;
  }
}
