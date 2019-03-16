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

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    RenderSpriteComponent renderSpriteComponent;
    AnimationComponent animationComponent;

    Tuple(Entity entity, AnimationComponent animationComponent, RenderSpriteComponent renderSpriteComponent) {
      this.entity = entity;
      this.animationComponent = animationComponent;
      this.renderSpriteComponent = renderSpriteComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var sprite = entity.getComponent(RenderSpriteComponent.class);
      var animationComponent = entity.getComponent(AnimationComponent.class);
      if (sprite != null && animationComponent != null) {
        return new Tuple(entity, animationComponent, sprite);
      }

      return null;
    }
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {
    t.renderSpriteComponent.flipX = t.animationComponent.flipX;
    t.renderSpriteComponent.spriteSource = t.animationComponent.animation.currentFrame(tickTime,
            t.animationComponent.currentAnimationStartTimeTick, t.animationComponent.animationSpeedOffsetInTicks);
  }
}