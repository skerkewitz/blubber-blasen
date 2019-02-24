package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.GroundDataComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;

/**
 * A system to render all SpriteComponents.
 */
public class GroundDataSystemSystem extends BaseComponentSystem<GroundDataSystemSystem.Tuple, GroundDataSystemSystem.TupleFactory> {

  public GroundDataSystemSystem() {
    super(new GroundDataSystemSystem.TupleFactory());
  }

  @Override
  public void execute(int tickTime, Tuple t, World world) {

    /* Point below feet. */
//    var pointBelowFeet = new Point2i(t.transformComponent.position.x, t.transformComponent.position.y + 1);

    /* We are on ground if feet are in free space and point below is in solid. */
    Point2i position = t.transformComponent.position;
    boolean isOnGroundLeft = checkGround(world, position.plus(new Point2i(t.groundDataComponent.leftOffset, t.groundDataComponent.heightOffset)));
    boolean isOnGroundRight = checkGround(world, position.plus(new Point2i(t.groundDataComponent.rightOffset, t.groundDataComponent.heightOffset)));
    t.groundDataComponent.isOnGround = isOnGroundLeft || isOnGroundRight;
  }

  private boolean checkGround(World world, Point2i position) {
    Tile lastTile = world.getTileAtPosition(position.x, position.y);
    Tile newTile = world.getTileAtPosition(position.x, position.y + 1);
    return !lastTile.isSolid() && newTile.isSolid();
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