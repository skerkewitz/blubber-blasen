package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.ZenChan;
import de.skerkewitz.blubberblase.esc.component.*;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.Optional;

/**
 * A system to render all SpriteComponents.
 */
public class AiSystem extends BaseComponentSystem<AiSystem.Tuple, AiSystem.TupleFactory> {

  public AiSystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world) {
    TransformComponent transformComponent = t.transformComponent;
    MovementComponent movementComponent = t.movementComponent;

    AiComponent aiComponent = t.aiComponent;

    if (aiComponent instanceof AiBubbleComponent) {
      AiBubbleComponent aiBubbleComponent = (AiBubbleComponent) aiComponent;

      if (aiBubbleComponent.currentState == AiBubbleComponent.State.SHOOT) {

        /* Check for collisions with enemy. */
        final Entity bubbleEntity = t.entity;
        CollisionComponent collisionComponent = bubbleEntity.getComponent(CollisionComponent.class);
        if (collisionComponent.hasCollission()) {
          /* Search for a monster collision. */
          Optional<Entity> enemy = collisionComponent.getCollisions().filter(entity -> entity instanceof ZenChan).findFirst();
          if (enemy.isPresent()) {
            final Entity enemyEntity = enemy.get();

            /* Hit the enemy. Remove enemy and replace this bubble with floating bubble. */
            enemyEntity.expired();
            bubbleEntity.expired();

            world.prepareSpawnAtTime(tickTime, EntityFactory.spawnCaptureBubble(tickTime, transformComponent.position));

            return;
          }
        }

        if (aiBubbleComponent.getStateTime(tickTime) > 2 * 4) {
          collisionComponent.removeCollideWithLayer(CollisionComponent.Layer.ENEMY);
          aiBubbleComponent.setState(tickTime, AiBubbleComponent.State.FLOAT);
          movementComponent.setMovementDirection(MoveableLegacyEntity.MoveDirection.Up, tickTime);
          movementComponent.speed = 0.5f;
        }

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