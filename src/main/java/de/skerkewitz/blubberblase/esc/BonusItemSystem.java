package de.skerkewitz.blubberblase.esc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.Optional;

/**
 * A common to render all SpriteComponents.
 */
public class BonusItemSystem extends BaseComponentSystem<BonusItemSystem.Tuple, BonusItemSystem.TupleFactory> {

  /**
   * Declares the component needed by this common.
   */
  static class Tuple implements ComponentSystem.Tuple {
    final Entity entity;
    final BonusItemComponent bonusItemComponent;
    final CollisionComponent collisionComponent;
    final TransformComponent transformComponent;

    Tuple(Entity entity, BonusItemComponent bonusItemComponent, CollisionComponent collisionComponent, TransformComponent transformComponent) {
      this.entity = entity;
      this.bonusItemComponent = bonusItemComponent;
      this.collisionComponent = collisionComponent;
      this.transformComponent = transformComponent;
    }
  }

  private Sound sfxBonusItemCollected = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx-bonus-item-collected.wav"));

  public BonusItemSystem() {
    super(new BonusItemSystem.TupleFactory());
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var bonusItemComponent = entity.getComponent(BonusItemComponent.class);
      var collisionComponent = entity.getComponent(CollisionComponent.class);
      var transformComponent = entity.getComponent(TransformComponent.class);
      if (bonusItemComponent != null && collisionComponent != null) {
        return new Tuple(entity, bonusItemComponent, collisionComponent, transformComponent);
      }
      return null;
    }
  }

  public static final int POINTS_DIAMOND = 500;

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    /* Check for collisions with enemy. */
    final CollisionComponent collisionComponent = t.entity.getComponent(CollisionComponent.class);

    if (collisionComponent.hasCollission()) {
      /* Search for a monster collision. */
      final Optional<Entity> player = collisionComponent.getCollisions().filter(entity -> entity.hasComponent(PlayerComponent.class)).findFirst();
      if (player.isPresent()) {
        t.entity.expired();
        sfxBonusItemCollected.play();
        context.scorePlayer1 += POINTS_DIAMOND;
        world.addEntity(EntityFactory.spawnPointPickup(tickTime, t.transformComponent.position.plus(0, -15)));
        return;
      }
      throw new IllegalStateException("Trap bubble collision with unknown entity " + collisionComponent.getCollisions());
    }
  }
}