package de.skerkewitz.blubberblase.esc.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.Bubble;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.GamePlay;
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

  private Sound sfxBurstTrapBubble = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_coin_double7.wav"));

  public AiBubbleSystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    switch (t.aiBubbleComponent.type) {
      case NORMAL:
        handleNormalBubble(tickTime, world, t);
        break;
      case TRAP:
        handleTrapBubble(tickTime, world, t);
        break;
    }
  }

  private void handleNormalBubble(int tickTime, World world, Tuple t) {

    final AiBubbleComponent aiBubbleComponent = t.aiBubbleComponent;
    final TransformComponent transformComponent = t.transformComponent;

    if (aiBubbleComponent.state == AiBubbleComponent.State.SHOOT) {
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

      if (aiBubbleComponent.getStateTime(tickTime) > 2 * 4) {
        collisionComponent.removeCollideWithLayer(CollisionComponent.Layer.ENEMY);
        aiBubbleComponent.setState(tickTime, AiBubbleComponent.State.FLOAT);

        final MovementComponent movementComponent = t.movementComponent;
        movementComponent.setMovementDirection(MoveDirection.Up, tickTime);
        movementComponent.speed = 0.5f;
      }
    }
  }

  private void handleTrapBubble(int tickTime, World world, Tuple t) {

    /* Check if we need to burst the bubble */
    final int ageFrameCount = LifeTimeUtil.getAge(tickTime, world, t.entity.getComponent(LifeTimeComponent.class));

    /* Should it blink. */
    t.entity.getComponent(SpriteComponent.class).visible = true;
    if (ageFrameCount > Bubble.MAX_LIFETIME_BEFORE_BURST - GamePlay.PRE_ACTION_INDICATION_FRAMECOUNT) {
      t.entity.getComponent(SpriteComponent.class).visible = tickTime / 10 % 2 == 0;
    }


    /* Did it burst? */
    if (ageFrameCount > Bubble.MAX_LIFETIME_BEFORE_BURST) {
      t.entity.expired();
      Entity entity = EntityFactory.spawnZenChan(t.transformComponent.position, tickTime, MoveDirection.Left, true);
      EnemyUtil.setupDidEscapeTrapBubble(entity.getComponent(EnemyComponent.class));
      world.addEntity(entity);
      return;
    }

    /* Check for collisions with enemy. */
    final CollisionComponent collisionComponent = t.entity.getComponent(CollisionComponent.class);

    if (collisionComponent.hasCollission()) {
      /* Search for a monster collision. */
      Optional<Entity> player = collisionComponent.getCollisions().filter(entity -> entity.hasComponent(PlayerComponent.class)).findFirst();
      if (player.isPresent()) {
        t.entity.expired();
        sfxBurstTrapBubble.play();
        return;
      }
      throw new IllegalStateException("Trap bubble collision with unknown entity " + collisionComponent.getCollisions());
    }
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    AiBubbleComponent aiBubbleComponent;
    TransformComponent transformComponent;
    MovementComponent movementComponent;

    Tuple(Entity entity, TransformComponent transformComponent, MovementComponent movementComponent, AiBubbleComponent aiBubbleComponent) {
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
      var aiComponent = entity.getComponent(AiBubbleComponent.class);
      if (movementComponent != null && transformComponent != null && aiComponent != null) {
        return new Tuple(entity, transformComponent, movementComponent, aiComponent);
      } else {
        return null;
      }
    }
  }


}