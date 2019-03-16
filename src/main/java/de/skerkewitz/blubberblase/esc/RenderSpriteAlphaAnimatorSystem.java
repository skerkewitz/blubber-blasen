package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.RenderSpriteAlphaAnimatorComponent;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
 */
public class RenderSpriteAlphaAnimatorSystem extends BaseComponentSystem<RenderSpriteAlphaAnimatorSystem.Tuple, RenderSpriteAlphaAnimatorSystem.TupleFactory> {

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    RenderSpriteComponent renderSpriteComponent;
    RenderSpriteAlphaAnimatorComponent renderSpriteAlphaAnimatorComponent;

    Tuple(Entity entity, RenderSpriteComponent renderSpriteComponent, RenderSpriteAlphaAnimatorComponent renderSpriteAlphaAnimatorComponent) {
      this.entity = entity;
      this.renderSpriteComponent = renderSpriteComponent;
      this.renderSpriteAlphaAnimatorComponent = renderSpriteAlphaAnimatorComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var renderSpriteComponent = entity.getComponent(RenderSpriteComponent.class);
      var renderSpriteAlphaAnimatorComponent = entity.getComponent(RenderSpriteAlphaAnimatorComponent.class);
      if (renderSpriteComponent != null && renderSpriteAlphaAnimatorComponent != null) {
        return new Tuple(entity, renderSpriteComponent, renderSpriteAlphaAnimatorComponent);
      }
      return null;
    }
  }


  public RenderSpriteAlphaAnimatorSystem() {
    super(new RenderSpriteAlphaAnimatorSystem.TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    var duration = t.renderSpriteAlphaAnimatorComponent.endTickTime - t.renderSpriteAlphaAnimatorComponent.startTickTime;
    var deltaTickTime = tickTime - t.renderSpriteAlphaAnimatorComponent.startTickTime;
    var delta = Math.min(1.0f, (1.0f / duration) * deltaTickTime);

    var lerpDelta = t.renderSpriteAlphaAnimatorComponent.interpolation.apply(delta);

    var deltaAlpha = (t.renderSpriteAlphaAnimatorComponent.endAlpha - t.renderSpriteAlphaAnimatorComponent.startAlpha) * lerpDelta;

    t.renderSpriteComponent.setAlpha(t.renderSpriteAlphaAnimatorComponent.startAlpha + deltaAlpha);
  }
}