package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.LifeTimeComponent;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A system to render all SpriteComponents.
 */
public class LifeTimeSystem extends BaseComponentSystem<LifeTimeSystem.Tuple, LifeTimeSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(LifeTimeSystem.class);

  public LifeTimeSystem() {
    super(new LifeTimeSystem.TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world) {
    t.lifetimeComponent.lifeTimeTC++;
    if (t.lifetimeComponent.maxLifeTimeTC > 0 && t.lifetimeComponent.lifeTimeTC > t.lifetimeComponent.maxLifeTimeTC) {
      t.entity.expired();
    }
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