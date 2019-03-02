package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.util.LifeTimeUtil;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class LifeTimeSystem extends BaseComponentSystem<LifeTimeSystem.Tuple, LifeTimeSystem.TupleFactory> {

  public LifeTimeSystem() {
    super(new LifeTimeSystem.TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    int ageFrameCount = LifeTimeUtil.getAge(tickTime, world, t.lifetimeComponent);
    if (ageFrameCount > t.lifetimeComponent.maxLifeTimeFrameCount) {
      t.entity.expired();
    }
  }

  @Override
  public Stream<Tuple> getTuples(Stream<Entity> stream) {
    return super.getTuples(stream).filter(t -> t.lifetimeComponent.maxLifeTimeFrameCount >= 0);
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    LifeTimeComponent lifetimeComponent;

    Tuple(Entity entity, LifeTimeComponent lifetimeComponent) {
      this.entity = entity;
      this.lifetimeComponent = lifetimeComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var lifetimeComponent = entity.getComponent(LifeTimeComponent.class);
      if (lifetimeComponent != null) {
        return new Tuple(entity, lifetimeComponent);
      }
      return null;
    }
  }
}