package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.enora2d.core.ecs.component.LifeTimeComponent;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class LifeTimeSystem {

  private static final Logger logger = LogManager.getLogger(LifeTimeSystem.class);

  public void update(int tickTime, Stream<Entity> stream) {
    getTuples(stream)
            .forEach(tuple -> thinkEntity(tickTime, tuple));
  }

  private void thinkEntity(int tickTime, Tuple t) {
    t.lifetimeComponent.lifeTimeTC++;
    if (t.lifetimeComponent.maxLifeTimeTC > 0 && t.lifetimeComponent.lifeTimeTC > t.lifetimeComponent.maxLifeTimeTC) {
      t.entity.expired();
    }
  }

  private Stream<Tuple> getTuples(Stream<Entity> stream) {
    return stream.map(Tuple::map).filter(Objects::nonNull);
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple {
    Entity entity;
    LifeTimeComponent lifetimeComponent;

    Tuple(Entity entity, LifeTimeComponent lifetimeComponent) {
      this.entity = entity;
      this.lifetimeComponent = lifetimeComponent;
    }

    static Tuple map(Entity entity) {
      var lifetimeComponent = entity.getComponent(LifeTimeComponent.class);
      if (lifetimeComponent != null) {
        return new Tuple(entity, lifetimeComponent);
      }
      return null;
    }
  }
}