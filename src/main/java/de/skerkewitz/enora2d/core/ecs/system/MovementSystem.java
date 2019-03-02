package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.blubberblase.esc.component.MovementComponent;
import de.skerkewitz.blubberblase.esc.component.StateBaseBubbleComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.Entity;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class MovementSystem {

  public void update(int tickTime, Stream<Entity> stream) {
    getTuples(stream)
            .forEach(tuple -> moveEntity(tuple.transformComponent, tuple.movementComponent));
  }

  private void moveEntity(TransformComponent transformComponent, MovementComponent movementComponent) {

    switch (movementComponent.currentMoveDirection) {
      case Up:
        move(0, -1, transformComponent, movementComponent);
        break;
      case Left:
        move(-1, 0, transformComponent, movementComponent);
        break;
      case Right:
        move(+1, 0, transformComponent, movementComponent);
        break;
      case Down:
        move(0, 1, transformComponent, movementComponent);
        break;
      case Idle:
        break;
    }
  }

  /**
   * Move the entity the given amount in the width and height direction.
   * <p>
   * This method will call hasCollided() internaly to check the entity movement again the world.
   *
   * @param xa
   * @param ya
   */
  void move(int xa, int ya, TransformComponent targetTransformComponent, MovementComponent movementComponent) {

    movementComponent.numSteps++;

    /* Update player position. */
    final Point2f position = targetTransformComponent.position;
    position.x += xa * movementComponent.speed;
    position.y += ya * movementComponent.speed;
  }

  private Stream<Tuple> getTuples(Stream<Entity> stream) {
    return stream.map(Tuple::map).filter(Objects::nonNull);
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple {
    Entity entity;
    TransformComponent transformComponent;
    MovementComponent movementComponent;

    Tuple(Entity entity, TransformComponent transformComponent, MovementComponent movementComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.movementComponent = movementComponent;
    }

    static Tuple map(Entity entity) {
      var movementComponent = entity.getComponent(MovementComponent.class);
      var transformComponent = entity.getComponent(TransformComponent.class);
      if (movementComponent != null && transformComponent != null && entity.hasComponent(StateBaseBubbleComponent.class)) {
        return new Tuple(entity, transformComponent, movementComponent);
      } else {
        return null;
      }
    }
  }
}