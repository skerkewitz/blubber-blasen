package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.blubberblase.esc.component.MovementComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class MovementSystem {

  private static final Logger logger = LogManager.getLogger(MovementSystem.class);

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
    int speed = 1;
    Point2i position = targetTransformComponent.position;
    position.x += xa * speed;
    position.y += ya * speed;
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
      if (movementComponent != null && transformComponent != null) {
        return new Tuple(entity, transformComponent, movementComponent);
      } else {
        return null;
      }
    }
  }
}