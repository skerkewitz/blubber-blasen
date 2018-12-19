package de.skerkewitz.enora2d.core.game.level;

import de.skerkewitz.enora2d.core.entity.Entity;
import de.skerkewitz.enora2d.core.entity.EntityContainer;
import de.skerkewitz.enora2d.core.entity.MoveableEntity;
import de.skerkewitz.enora2d.core.entity.PlayerMP;
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

    Tile lastTile = backgroundLayer.getTile((px + x) >> 3, (py + y) >> 3);
    Tile newTile = backgroundLayer.getTile((px + x + xa) >> 3, (py + y + ya) >> 3);
    return !lastTile.equals(newTile) && newTile.isSolid();
  }

  public void spawnEntity(Entity entity) {
    entityContainer.addEntity(entity);
  }

  public void removePlayerMP(String username) {
//    int index = 0;
//    for (Entity e : getEntities()) {
//      if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
//        break;
//      }
//      index++;
//    }
//    this.getEntities().remove(index);
  }

  public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, MoveableEntity.MoveDirection movingDir) {

    var player = entityContainer.findFirst((Entity e) -> e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username));
    if (player.isPresent()) {
      PlayerMP playerMP = (PlayerMP) player.get();
      playerMP.posX = x;
      playerMP.posY = y;
      playerMP.setMoving(isMoving);
      playerMP.setNumSteps(numSteps);
      playerMP.setMovingDir(movingDir);
    }
  }

  public EntityContainer getEntityContainer() {
    return entityContainer;
  }
}
