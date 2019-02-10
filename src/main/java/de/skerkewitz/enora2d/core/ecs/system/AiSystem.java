package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.enora2d.core.ecs.component.AiComponent;
import de.skerkewitz.enora2d.core.ecs.component.MovementComponent;
import de.skerkewitz.enora2d.core.ecs.component.TransformComponent;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class AiSystem {

  private static final Logger logger = LogManager.getLogger(AiSystem.class);

  public void update(int tickTime, Stream<Entity> stream) {
    getTuples(stream)
            .forEach(tuple -> thinkEntity(tickTime, tuple.transformComponent, tuple.movementComponent));
  }

  private void thinkEntity(int tickTime, TransformComponent transformComponent, MovementComponent movementComponent) {
    if (movementComponent.currentMoveDirection != MoveableLegacyEntity.MoveDirection.Up && movementComponent.numSteps > 8 * 4) {
      movementComponent.setMovementDirection(MoveableLegacyEntity.MoveDirection.Up, tickTime);
    }
  }

  private Stream<Tuple> getTuples(Stream<Entity> stream) {
    return stream.map(Tuple::map).filter(Objects::nonNull);
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple {
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

    static Tuple map(Entity entity) {
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