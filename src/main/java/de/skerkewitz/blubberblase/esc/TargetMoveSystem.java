package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * Moves the {@link TransformComponent} to a target position.
 */
public class TargetMoveSystem extends BaseComponentSystem<TargetMoveSystem.Tuple, TargetMoveSystem.TupleFactory> {

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    TargetMoveComponent targetMoveComponent;

    Tuple(Entity entity, TransformComponent transformComponent, TargetMoveComponent targetMoveComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.targetMoveComponent = targetMoveComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var transformComponent = entity.getComponent(TransformComponent.class);
      var targetMoveSystem = entity.getComponent(TargetMoveComponent.class);
      if (transformComponent != null && targetMoveSystem != null) {
        return new Tuple(entity, transformComponent, targetMoveSystem);
      }
      return null;
    }
  }


  public TargetMoveSystem() {
    super(new TargetMoveSystem.TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    var targetX = (t.targetMoveComponent.position.x);
    var targetY = (t.targetMoveComponent.position.y);

    Point2f position = t.transformComponent.position;
    var posX = (int) (position.x);
    var posY = (int) (position.y);

    if (targetX == posX && targetY == posY) {
      t.entity.removeComponent(t.targetMoveComponent);
    }

    if (targetX < position.x) {
      position.x -= 1;
    } else if (targetX > position.x) {
      position.x += 1;
    }

    if (targetY < position.y) {
      position.y -= 1;
    } else if (targetY > position.y) {
      position.y += 1;
    }
  }
}