package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A common to render all SpriteComponents.
 */
public class GroundDataSystem extends BaseComponentSystem<GroundDataSystem.Tuple, GroundDataSystem.TupleFactory> {

  public GroundDataSystem() {
    super(new GroundDataSystem.TupleFactory());
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    /* Point below feet. */
//    var pointBelowFeet = new Point2i(t.transformComponent.position.x, t.transformComponent.position.y + 1);

    /* We are on ground if feet are in free space and point below is in solid. */
    final Point2i position = t.transformComponent.position.toPoint2i();
    boolean isOnGroundLeft = LevelUtils.checkGround(position.plus(new Point2i(t.groundDataComponent.leftOffset, t.groundDataComponent.heightOffset)), world);
    boolean isOnGroundRight = LevelUtils.checkGround(position.plus(new Point2i(t.groundDataComponent.rightOffset, t.groundDataComponent.heightOffset)), world);
    t.groundDataComponent.isOnGround = isOnGroundLeft || isOnGroundRight;
  }

  /**
   * Declares the component needed by this common.
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