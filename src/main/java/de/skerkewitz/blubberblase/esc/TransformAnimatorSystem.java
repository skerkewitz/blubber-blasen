package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
 */
public class TransformAnimatorSystem extends BaseComponentSystem<TransformAnimatorSystem.Tuple, TransformAnimatorSystem.TupleFactory> {

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    TransformAnimatorComponent transformAnimatorComponent;

    Tuple(Entity entity, TransformComponent transformComponent, TransformAnimatorComponent transformAnimatorComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.transformAnimatorComponent = transformAnimatorComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var transformComponent = entity.getComponent(TransformComponent.class);
      var transformAnimatorComponent = entity.getComponent(TransformAnimatorComponent.class);
      if (transformComponent != null && transformAnimatorComponent != null) {
        return new Tuple(entity, transformComponent, transformAnimatorComponent);
      }
      return null;
    }
  }


  public TransformAnimatorSystem() {
    super(new TransformAnimatorSystem.TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    var duration = t.transformAnimatorComponent.endFrameCount - t.transformAnimatorComponent.startFrameCount;
    var deltaTickTime = tickTime - t.transformAnimatorComponent.startFrameCount;
    var delta = Math.min(1.0f, (1.0f / duration) * deltaTickTime);

    var lerpDelta = t.transformAnimatorComponent.interpolation.apply(delta);

    var deltaX = (t.transformAnimatorComponent.endPosition.x - t.transformAnimatorComponent.startPosition.x) * lerpDelta;
    var deltaY = (t.transformAnimatorComponent.endPosition.y - t.transformAnimatorComponent.startPosition.y) * lerpDelta;

//    System.out.println("tt: " + tickTime + " stt: " + t.transformAnimatorComponent.startFrameCount
//      + " ett: " + t.transformAnimatorComponent.endFrameCount
//      + " dtt: " + deltaTickTime
//      + " delta " + delta
//      + " ldelta " + lerpDelta
//      + " dx : " + deltaX
//      + " dy : " + deltaY);

    t.transformComponent.position.x = t.transformAnimatorComponent.startPosition.x + deltaX;
    t.transformComponent.position.y = t.transformAnimatorComponent.startPosition.y + deltaY;
  }
}