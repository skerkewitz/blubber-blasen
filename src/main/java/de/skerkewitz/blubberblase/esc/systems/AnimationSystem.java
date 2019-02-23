package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.AnimationComponent;
import de.skerkewitz.blubberblase.esc.component.SpriteComponent;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A system to render all SpriteComponents.
 */
public class AnimationSystem extends BaseComponentSystem<AnimationSystem.Tuple, AnimationSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(AnimationSystem.class);

  public AnimationSystem() {
    super(new AnimationSystem.TupleFactory());
  }

  @Override
  public void execute(int tickTime, Tuple t, World world) {
    t.spriteComponent.flipX = t.animationComponent.flipX;
    t.spriteComponent.renderSprite = t.animationComponent.animation.currentFrame(tickTime, t.animationComponent.currentAnimationStartTimeTick);
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    SpriteComponent spriteComponent;
    AnimationComponent animationComponent;

    Tuple(Entity entity, AnimationComponent animationComponent, SpriteComponent spriteComponent) {
      this.entity = entity;
      this.animationComponent = animationComponent;
      this.spriteComponent = spriteComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var sprite = entity.getComponent(SpriteComponent.class);
      var animationComponent = entity.getComponent(AnimationComponent.class);
      if (sprite != null && animationComponent != null) {
        return new Tuple(entity, animationComponent, sprite);
      }

      return null;
    }
  }
}