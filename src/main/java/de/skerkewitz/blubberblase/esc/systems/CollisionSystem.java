package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.CollisionComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class CollisionSystem extends BaseComponentSystem<CollisionSystem.Tuple, CollisionSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(CollisionSystem.class);

  public CollisionSystem() {
    super(new TupleFactory());
  }

  @Override
  public void executor(int tickTime, World world, Stream<Entity> stream) {

    final List<Tuple> tuples = getTuples(stream).collect(Collectors.toList());

    tuples.forEach(t -> t.collisionComponent.didCollide = false);

    /* We need at least two element to compare something. */
    if (tuples.size() < 2) {
      return;
    }

    for (int i = 0; i < tuples.size() - 1; i++) {
      final Tuple t = tuples.get(i);
      tuples.subList(i + 1, tuples.size()).stream().forEach(o -> execute(tickTime, t, o, world));
    }
  }

  private void execute(int tickTime, Tuple t, Tuple o, World world) {

    o.boundingBoxComponent.getBoundingBox();

    final Rect2i tbb = new Rect2i(t.transformComponent.position.plus(t.boundingBoxComponent.getBoundingBox().origin), t.boundingBoxComponent.getBoundingBox().size);
    final Rect2i obb = new Rect2i(o.transformComponent.position.plus(o.boundingBoxComponent.getBoundingBox().origin), o.boundingBoxComponent.getBoundingBox().size);

    boolean collide = BoundingBoxUtil.collide(tbb, obb);
    t.collisionComponent.applyCollide(collide);
    o.collisionComponent.applyCollide(collide);
  }

  public void execute(int tickTime, Tuple t, World world) {
//    TransformComponent transformComponent = t.transformComponent;
//    MovementComponent movementComponent = t.movementComponent;
//    if (movementComponent.currentMoveDirection != MoveableLegacyEntity.MoveDirection.Up && movementComponent.numSteps > 8 * 4) {
//      movementComponent.setMovementDirection(MoveableLegacyEntity.MoveDirection.Up, tickTime);
//    }
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    CollisionComponent collisionComponent;
    BoundingBoxComponent boundingBoxComponent;

    Tuple(Entity entity, TransformComponent transformComponent, CollisionComponent collisionComponent, BoundingBoxComponent boundingBoxComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.collisionComponent = collisionComponent;
      this.boundingBoxComponent = boundingBoxComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var transformComponent = entity.getComponent(TransformComponent.class);
      var collisionComponent = entity.getComponent(CollisionComponent.class);
      var boundingBoxComponent = entity.getComponent(BoundingBoxComponent.class);
      if (/*collisionComponent != null  && */transformComponent != null && boundingBoxComponent != null) {
        return new Tuple(entity, transformComponent, collisionComponent, boundingBoxComponent);
      } else {
        return null;
      }
    }
  }


  private static class BoundingBoxUtil {
    public static boolean collide(Rect2i tbb, Rect2i obb) {
      return !((tbb.origin.x + tbb.size.width < obb.origin.x) || (obb.origin.x + obb.size.width < tbb.origin.x)
              || (tbb.origin.y + tbb.size.height < obb.origin.y) || (obb.origin.y + obb.size.height < tbb.origin.y));
    }
  }
}