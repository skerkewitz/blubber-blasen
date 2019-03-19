package de.skerkewitz.enora2d.core.game.world;

import de.gierzahn.editor.map.AirflowDirection;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Point2i;
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
  private Entity playerScoreEntity;

  private final int levelStartFrameCount;

  public World(StaticMapContent staticMapContent, int frameCount) {
    entityContainer = new EntityContainer();
    this.staticMapContent = staticMapContent;
    this.levelStartFrameCount = frameCount;
  }

  public void tick(int tickTime, GameContext context) {
    spawnSheduler.spawnEntities(tickTime, this);
    getEntityContainer().purgeExpired();
  }

  public int getWorldFrameCount(int tickTime) {
    return tickTime - levelStartFrameCount;
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

  /**
   * Returns the tile at the given pixel position.
   */
  public boolean isSolidAtPosition(int x, int y) {
    return staticMapContent.isSolidGround(x >> 3, y >> 3);
  }


  public void addEntity(Entity entity) {
    entityContainer.addEntity(entity);
  }


  public EntityContainer getEntityContainer() {
    return entityContainer;
  }


  public Entity getPlayerEntity() {
    return playerEntity;
  }

  private void setPlayerEntity(Entity playerEntity) {
    this.playerEntity = playerEntity;
  }

  public void addPlayer(Entity playerEntity, Entity playerScoreEntity) {
    addEntity(playerEntity);
    setPlayerEntity(playerEntity);
    addEntity(playerScoreEntity);
    this.playerScoreEntity = playerScoreEntity;
  }

  public boolean canGapJumpAtPosition(int x, int y, int moveDir) {

    int tx = x / 8;
    int ty = y / 8;

    /* Maximum Gap distance is 3. Find a solid tile so we can jump. Very naive approach*/
    for (var i = 0; i < 4; i++) {
      if (staticMapContent.getTile(tx + (moveDir * i), ty - 1).isSolid()) {
        return false;
      }

      if (staticMapContent.getTile(tx + (moveDir * i), ty).isSolid()) {
        return true;
      }
    }

    return false;
  }

  public boolean canJumpUp(int x, int y) {
    int tx = x / 8;
    int ty = y / 8;

    if (staticMapContent.getTile(tx, ty - 1).isSolid()) {
      return false;
    }

    if (staticMapContent.getTile(tx, ty - 2).isSolid()) {
      return false;
    }

    if (staticMapContent.getTile(tx, ty - 3).isSolid()) {
      return false;
    }

    if (staticMapContent.getTile(tx, ty - 4).isSolid()) {
      return false;
    }

    if (!staticMapContent.getTile(tx, ty - 5).isSolid()) {
      return false;
    }

    return !staticMapContent.getTile(tx, ty - 6).isSolid();
  }

  public Point2i convertWorldToTileSpace(Point2f pos) {
    return new Point2i((int) (pos.x) >> 3, (int) (pos.y) >> 3);
  }


  public AirflowDirection getAirflowAt(Point2f position) {
    var tilePos = convertWorldToTileSpace(position);
    return staticMapContent.getAirflowAt(tilePos);
  }

  public Entity getPlayerScoreEntity() {
    return playerScoreEntity;
  }
}
