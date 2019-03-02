package de.skerkewitz.blubberblase.esc.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.Optional;

/**
 * A system to render all SpriteComponents.
 */
public class BonusItemSystem extends BaseComponentSystem<BonusItemSystem.Tuple, BonusItemSystem.TupleFactory> {

  private Sound sfxBonusItemCollected = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx-bonus-item-collected.wav"));

  public BonusItemSystem() {
    super(new BonusItemSystem.TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    /* Check for collisions with enemy. */
    final CollisionComponent collisionComponent = t.entity.getComponent(CollisionComponent.class);

    if (collisionComponent.hasCollission()) {
      /* Search for a monster collision. */
      final Optional<Entity> player = collisionComponent.getCollisions().filter(entity -> entity.hasComponent(PlayerComponent.class)).findFirst();
      if (player.isPresent()) {
        t.entity.expired();
        sfxBonusItemCollected.play();
        return;
      }
      throw new IllegalStateException("Trap bubble collision with unknown entity " + collisionComponent.getCollisions());
    }
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    final Entity entity;
    final BonusItemComponent bonusItemComponent;
    final CollisionComponent collisionComponent;

    Tuple(Entity entity, BonusItemComponent bonusItemComponent, CollisionComponent collisionComponent) {
      this.entity = entity;
      this.bonusItemComponent = bonusItemComponent;
      this.collisionComponent = collisionComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var bonusItemComponent = entity.getComponent(BonusItemComponent.class);
      var collisionComponent = entity.getComponent(CollisionComponent.class);
      if (bonusItemComponent != null && collisionComponent != null) {
        return new Tuple(entity, bonusItemComponent, collisionComponent);
      }
      return null;
    }
  }
}