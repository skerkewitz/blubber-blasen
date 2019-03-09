package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
 */
public class SoundSystem extends BaseComponentSystem<SoundSystem.Tuple, SoundSystem.TupleFactory> {

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    SoundComponent soundComponent;

    Tuple(Entity entity, SoundComponent soundComponent) {
      this.entity = entity;
      this.soundComponent = soundComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var soundComponent = entity.getComponent(SoundComponent.class);
      if (soundComponent != null) {
        return new Tuple(entity, soundComponent);
      }
      return null;
    }
  }


  public SoundSystem() {
    super(new SoundSystem.TupleFactory());
    this.componentPredicate = tuple -> tuple.soundComponent.shouldPlay;
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {
    t.soundComponent.sound.play(t.soundComponent.volume);
    t.soundComponent.shouldPlay = false;
  }
}