package de.skerkewitz.blubberblase;

import de.skerkewitz.blubberblase.esc.systems.*;
import de.skerkewitz.enora2d.core.ecs.LegacyEntity;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.MovementSystem;
import de.skerkewitz.enora2d.core.game.level.World;

public class MainWorld extends World {

  private MovementSystem movementSystem = new MovementSystem();
  private AiSystem aiSystem = new AiSystem();
  private LifeTimeSystem lifeTimeSystem = new LifeTimeSystem();
  private AnimationSystem animationSystem = new AnimationSystem();
  private GroundDataSystemSystem groundDataSystemSystem = new GroundDataSystemSystem();
  private InputSystem inputSystem = new InputSystem();

  public void tick(int tickTime) {

    /* Update life time of entities and purge dead entities. */
    lifeTimeSystem.update(tickTime, this, entityContainer.stream());
    entityContainer.purgeExpired();

    groundDataSystemSystem.update(tickTime, this, entityContainer.stream());
    inputSystem.update(tickTime, this, entityContainer.stream());

    /* Tick legacy entities. */
    entityContainer.forEach((Entity e) -> {
      if (e instanceof LegacyEntity) {
        ((LegacyEntity) e).tick(this, tickTime);
      }
    });
    entityContainer.purgeExpired();

    aiSystem.update(tickTime, this, entityContainer.stream());
    movementSystem.update(tickTime, entityContainer.stream());

    animationSystem.update(tickTime, this, entityContainer.stream());

    /* Tick background layer. */
    backgroundLayer.tick(tickTime);
  }
}
