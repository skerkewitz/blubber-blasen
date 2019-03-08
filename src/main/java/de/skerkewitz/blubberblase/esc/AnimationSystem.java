package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
 */
public class AnimationSystem extends BaseComponentSystem<AnimationSystem.Tuple, AnimationSystem.TupleFactory> {

  public AnimationSystem() {
    super(new AnimationSystem.TupleFactory());
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {
    t.spriteComponent.flipX = t.animationComponent.flipX;
    t.spriteComponent.renderSprite = t.animationComponent.animation.currentFrame(tickTime,
            t.animationComponent.currentAnimationStartTimeTick, t.animationComponent.animationSpeedOffsetInTicks);
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