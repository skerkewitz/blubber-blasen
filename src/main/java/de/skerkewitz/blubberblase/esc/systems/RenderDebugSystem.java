package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.World;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import de.skerkewitz.enora2d.core.gfx.Renderer;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A system to render all SpriteComponents.
 */
public class RenderDebugSystem extends BaseComponentSystem<RenderDebugSystem.Tuple, RenderDebugSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(RenderDebugSystem.class);

  private Screen screen;
  private ImageDataContainer imageDataContainer = new ImageDataContainer();

  public RenderDebugSystem(Screen screen) {
    super(new RenderDebugSystem.TupleFactory());
    this.screen = screen;
  }

  @Override
  public void execute(int tickTime, Tuple t, World world) {
    TransformComponent transformComponent = t.transformComponent;
    BoundingBoxComponent boundingBoxComponent = t.boundingBoxComponent;

    Rect2i boundingBox = new Rect2i(t.transformComponent.position.plus(boundingBoxComponent.getBoundingBox().origin), boundingBoxComponent.getBoundingBox().size);
    Renderer.renderBoxOutline(screen.screenImageData, boundingBox, RgbColorPalette.encodeColorCode(555));
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    BoundingBoxComponent boundingBoxComponent;

    Tuple(Entity entity, TransformComponent transformComponent, BoundingBoxComponent boundingBoxComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.boundingBoxComponent = boundingBoxComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var transform = entity.getComponent(TransformComponent.class);
      var sprite = entity.getComponent(BoundingBoxComponent.class);
      if (sprite != null && transform != null) {
        return new Tuple(entity, transform, sprite);
      } else {
        return null;
      }
    }
  }
}