package de.skerkewitz.enora2d.core.game.level;

import de.skerkewitz.blubberblase.esc.systems.AiSystem;
import de.skerkewitz.blubberblase.esc.systems.AnimationSystem;
import de.skerkewitz.blubberblase.esc.systems.LifeTimeSystem;
import de.skerkewitz.enora2d.core.ecs.EntityContainer;
import de.skerkewitz.enora2d.core.ecs.LegacyEntity;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.MovementSystem;
import de.skerkewitz.enora2d.core.game.level.tiles.Tile;

public class Level {

  public BackgroundLayer backgroundLayer;

  private final EntityContainer entityContainer;

  private MovementSystem movementSystem = new MovementSystem();
  private AiSystem aiSystem = new AiSystem();
  private LifeTimeSystem lifeTimeSystem = new LifeTimeSystem();
  private AnimationSystem animationSystem = new AnimationSystem();


  public Level() {
    //backgroundLayer = new BackgroundLayer("/levels/water_test_level.png");
    entityContainer = new EntityContainer();
    backgroundLayer = new BackgroundLayer(null);
  }

  public void tick(int tickTime) {

    /* Update life time of entities and purge dead entities. */
    lifeTimeSystem.update(tickTime, entityContainer.stream());
    entityContainer.purgeExpired();

    /* Tick legacy entities. */
    entityContainer.forEach((Entity e) -> {
      if (e instanceof LegacyEntity) {
        ((LegacyEntity) e).tick(this, tickTime);
      }
    });
    entityContainer.purgeExpired();

    aiSystem.update(tickTime, entityContainer.stream());
    movementSystem.update(tickTime, entityContainer.stream());

    animationSystem.update(tickTime, entityContainer.stream());

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


  public void addEntity(Entity entity) {
    entityContainer.addEntity(entity);
  }


  public EntityContainer getEntityContainer() {
    return entityContainer;
  }


}
