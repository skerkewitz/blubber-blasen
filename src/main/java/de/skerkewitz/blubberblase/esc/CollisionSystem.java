package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.common.BoundingBoxUtil;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A common to render all SpriteComponents.
 */
public class CollisionSystem extends BaseComponentSystem<CollisionSystem.Tuple, CollisionSystem.TupleFactory> {

  public CollisionSystem() {
    super(new TupleFactory());
  }

  @Override
  public boolean executor(int tickTime, World world, Stream<Entity> stream, GameContext context) {

    final List<Tuple> tuples = getTuples(stream).collect(Collectors.toList());

    /* Clear all previous collisions. */
    tuples.forEach(t -> t.collisionComponent.clearCollisionSet());

    /* We need at least two element to compare something. */
    if (tuples.size() < 2) {
      return false;
    }

    for (int i = 0; i < tuples.size() - 1; i++) {
      final Tuple t = tuples.get(i);
      tuples.subList(i + 1, tuples.size()).stream().forEach(o -> execute(tickTime, t, o, world));
    }

    return true;
  }

  private void execute(int tickTime, Tuple t, Tuple o, World world) {

    var canTCollideWithO = t.collisionComponent.canCollideWith(o.collisionComponent);
    var canOCollideWithT = o.collisionComponent.canCollideWith(t.collisionComponent);

    if (!canTCollideWithO && !canOCollideWithT) {
      return;
    }

    final Rect2i tbb = new Rect2i(t.transformComponent.position.toPoint2i().plus(t.boundingBoxComponent.getBoundingBox().origin), t.boundingBoxComponent.getBoundingBox().size);
    final Rect2i obb = new Rect2i(o.transformComponent.position.toPoint2i().plus(o.boundingBoxComponent.getBoundingBox().origin), o.boundingBoxComponent.getBoundingBox().size);
    if (BoundingBoxUtil.doesOverlap(tbb, obb)) {
      if (canTCollideWithO) {
        t.collisionComponent.addCollide(o.entity);
      }

      if (canOCollideWithT) {
        o.collisionComponent.addCollide(t.entity);
      }
    }
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {
    throw new UnsupportedOperationException("Don't call me!");
  }

  /**
   * Declares the component needed by this common.
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
      if (collisionComponent != null && transformComponent != null && boundingBoxComponent != null) {
        return new Tuple(entity, transformComponent, collisionComponent, boundingBoxComponent);
      } else {
        return null;
      }
    }
  }


}