package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.Bubble;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.GamePlay;
import de.skerkewitz.blubberblase.entity.TrapBubble;
import de.skerkewitz.blubberblase.util.LifeTimeUtil;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.Optional;

/**
 * A system to render all SpriteComponents.
 */
public class AiBubbleSystem extends BaseComponentSystem<AiBubbleSystem.Tuple, AiBubbleSystem.TupleFactory> {


  public AiBubbleSystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    switch (t.aiBubbleComponent.type) {
      case NORMAL:
        handleNormalBubble(tickTime, world, t);
        break;
      case TRAP:
        handleTrapBubble(tickTime, world, t, context);
        break;
    }
  }

  private void handleNormalBubble(int tickTime, World world, Tuple t) {

    final StateBaseBubbleComponent aiBubbleComponent = t.aiBubbleComponent;
    final TransformComponent transformComponent = t.transformComponent;

    if (aiBubbleComponent.state == StateBaseBubbleComponent.State.SHOOT) {
      /* Check for collisions with enemy. */
      final CollisionComponent collisionComponent = t.entity.getComponent(CollisionComponent.class);

      if (collisionComponent.hasCollission()) {
        /* Search for a monster collision. */
        Optional<Entity> enemy = collisionComponent.getCollisions().filter(entity -> entity.hasComponent(EnemyComponent.class)).findFirst();
        if (enemy.isPresent()) {
          final Entity enemyEntity = enemy.get();

          /* Hit the enemy. Remove enemy and replace this bubble with floating bubble. */
          enemyEntity.expired();
          t.entity.expired();

          world.prepareSpawnAtTime(tickTime, EntityFactory.spawnTrapBubble(tickTime, transformComponent.position));

          return;
        }
      }

      if (aiBubbleComponent.getStateAge(tickTime) > 2 * 4) {
        collisionComponent.removeCollideWithLayer(CollisionComponent.Layer.ENEMY);
        aiBubbleComponent.setState(tickTime, StateBaseBubbleComponent.State.FLOAT);

        final MovementComponent movementComponent = t.movementComponent;
        movementComponent.setMovementDirection(MoveDirection.Up, tickTime);

        movementComponent.speed = Bubble.FLOATING_SPEED;
      }
    }

    /* Did it burst? */
    final int ageFrameCount = LifeTimeUtil.getAge(tickTime, world, t.entity.getComponent(LifeTimeComponent.class));
    if (ageFrameCount > Bubble.MAX_LIFETIME_BEFORE_BURST) {
      burstBubble(tickTime, world, t);
    }
  }

  private void handleTrapBubble(int tickTime, World world, Tuple t, GameContext context) {

    /* Check if we need to burst the bubble */
    final int ageFrameCount = LifeTimeUtil.getAge(tickTime, world, t.entity.getComponent(LifeTimeComponent.class));

    /* Should it blink. */
    t.entity.getComponent(RenderSpriteComponent.class).setVisible(true);
    if (ageFrameCount > TrapBubble.MAX_LIFETIME_BEFORE_BURST - GamePlay.PRE_ACTION_INDICATION_FRAMECOUNT) {
      t.entity.getComponent(RenderSpriteComponent.class).setVisible(tickTime / 10 % 2 == 0);
    }


    /* Did it burst? */
    if (ageFrameCount > TrapBubble.MAX_LIFETIME_BEFORE_BURST) {
      Entity entity = EntityFactory.spawnZenChan(t.transformComponent.position, tickTime, MoveDirection.Left, true);
      EnemyUtil.setupDidEscapeTrapBubble(entity.getComponent(EnemyComponent.class));
      world.addEntity(entity);
      burstBubble(tickTime, world, t);
      return;
    }

    /* Check for collisions with enemy. */
    final CollisionComponent collisionComponent = t.entity.getComponent(CollisionComponent.class);

    if (collisionComponent.hasCollission()) {
      /* Search for a monster collision. */
      Optional<Entity> player = collisionComponent.getCollisions().filter(entity -> entity.hasComponent(PlayerComponent.class)).findFirst();
      if (player.isPresent()) {
        world.addEntity(EntityFactory.spawnThrownEnemy(tickTime, t.transformComponent.position, player.get().getComponent(PlayerComponent.class).movingDir));
        burstBubble(tickTime, world, t);
        context.scorePlayer1 += 1000;
        return;
      }
      throw new IllegalStateException("Trap bubble collision with unknown entity " + collisionComponent.getCollisions());
    }
  }

  private void burstBubble(int tickTime, World world, Tuple t) {
    t.entity.expired();
    world.addEntity(EntityFactory.spawnBubbleBurst(tickTime, t.transformComponent.position.cloneCopy()));
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    StateBaseBubbleComponent aiBubbleComponent;
    TransformComponent transformComponent;
    MovementComponent movementComponent;

    Tuple(Entity entity, TransformComponent transformComponent, MovementComponent movementComponent, StateBaseBubbleComponent aiBubbleComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.movementComponent = movementComponent;
      this.aiBubbleComponent = aiBubbleComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var movementComponent = entity.getComponent(MovementComponent.class);
      var transformComponent = entity.getComponent(TransformComponent.class);
      var aiComponent = entity.getComponent(StateBaseBubbleComponent.class);
      if (movementComponent != null && transformComponent != null && aiComponent != null) {
        return new Tuple(entity, transformComponent, movementComponent, aiComponent);
      } else {
        return null;
      }
    }
  }


}