package de.skerkewitz.blubberblase.esc.systems;

import de.skerkewitz.enora2d.core.ecs.component.SpriteComponent;
import de.skerkewitz.enora2d.core.ecs.component.TransformComponent;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.gfx.ImageData;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import de.skerkewitz.enora2d.core.gfx.Renderer;
import de.skerkewitz.enora2d.core.gfx.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class RenderSpriteSystem extends BaseComponentSystem<RenderSpriteSystem.Tuple, RenderSpriteSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(RenderSpriteSystem.class);

  private Screen screen;
  private ImageDataContainer imageDataContainer = new ImageDataContainer();

  public RenderSpriteSystem(Screen screen) {
    super(new RenderSpriteSystem.TupleFactory());
    this.screen = screen;
  }

  @Override
  public void execute(int tickTime, Tuple tuple) {
    TransformComponent transformComponent = tuple.transformComponent;
    SpriteComponent sprite = tuple.spriteComponent;
    try {
      ImageData imageData = imageDataContainer.getResourceForName(sprite.renderSprite.namedResource);
      Renderer.renderSubImage(imageData, sprite.renderSprite.rect, sprite.colorPalette,
              screen.screenImageData, transformComponent.position, sprite.flipX, sprite.flipY);
    } catch (IOException e) {
      logger.error("Error rendering sprite because of: " + e, e);
    }
  }

  public Stream<Tuple> getTuples(Stream<Entity> stream) {
    return super.getTuples(stream).filter(tuple -> tuple.spriteComponent.renderSprite != null);
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    SpriteComponent spriteComponent;

    Tuple(Entity entity, TransformComponent transformComponent, SpriteComponent spriteComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.spriteComponent = spriteComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var sprite = entity.getComponent(SpriteComponent.class);
      var transform = entity.getComponent(TransformComponent.class);
      if (sprite != null && transform != null) {
        return new Tuple(entity, transform, sprite);
      } else {
        return null;
      }
    }
  }
}