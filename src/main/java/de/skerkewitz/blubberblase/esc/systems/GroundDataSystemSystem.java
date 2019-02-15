package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.GroundDataComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.game.level.tiles.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A system to render all SpriteComponents.
 */
public class GroundDataSystemSystem extends BaseComponentSystem<GroundDataSystemSystem.Tuple, GroundDataSystemSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(GroundDataSystemSystem.class);

  public GroundDataSystemSystem() {
    super(new GroundDataSystemSystem.TupleFactory());
  }

  @Override
  public void execute(int tickTime, Tuple t, Level level) {

    /* Point below feet. */
//    var pointBelowFeet = new Point2i(t.transformComponent.position.x, t.transformComponent.position.y + 1);

    /* We are on ground if feet are in free space and point below is in solid. */
    Tile lastTile = level.getTileAtPosition(t.transformComponent.position.x, t.transformComponent.position.y);
    Tile newTile = level.getTileAtPosition(t.transformComponent.position.x, t.transformComponent.position.y + 1);
    t.groundDataComponent.isOnGround = !lastTile.isSolid() && newTile.isSolid();
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    GroundDataComponent groundDataComponent;

    Tuple(Entity entity, TransformComponent transformComponent, GroundDataComponent groundDataComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.groundDataComponent = groundDataComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var transformComponent = entity.getComponent(TransformComponent.class);
      var groundDataComponent = entity.getComponent(GroundDataComponent.class);
      if (transformComponent != null && groundDataComponent != null) {
        return new Tuple(entity, transformComponent, groundDataComponent);
      }

      return null;
    }
  }
}