package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.*;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.game.level.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A system to render all SpriteComponents.
 */
public class AiSystem extends BaseComponentSystem<AiSystem.Tuple, AiSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(AiSystem.class);

  public AiSystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world) {
    TransformComponent transformComponent = t.transformComponent;
    MovementComponent movementComponent = t.movementComponent;

    AiComponent aiComponent = t.aiComponent;

    if (aiComponent instanceof AiBubbleComponent) {
      AiBubbleComponent aiBubbleComponent = (AiBubbleComponent) aiComponent;
      if (aiBubbleComponent.currentState == AiBubbleComponent.State.SHOOT && aiBubbleComponent.getStateTime(tickTime) > 2 * 4) {
        t.entity.getComponent(CollisionComponent.class).removeCollideWithLayer(CollisionComponent.Layer.ENEMY);
        aiBubbleComponent.setState(tickTime, AiBubbleComponent.State.FLOAT);
        movementComponent.setMovementDirection(MoveableLegacyEntity.MoveDirection.Up, tickTime);
        movementComponent.speed = 1;
      }
    }
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    AiComponent aiComponent;
    TransformComponent transformComponent;
    MovementComponent movementComponent;

    Tuple(Entity entity, TransformComponent transformComponent, MovementComponent movementComponent, AiComponent aiComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.movementComponent = movementComponent;
      this.aiComponent = aiComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var movementComponent = entity.getComponent(MovementComponent.class);
      var transformComponent = entity.getComponent(TransformComponent.class);
      var aiComponent = entity.getComponent(AiComponent.class);
      if (movementComponent != null && transformComponent != null && aiComponent != null) {
        return new Tuple(entity, transformComponent, movementComponent, aiComponent);
      } else {
        return null;
      }
    }
  }


}