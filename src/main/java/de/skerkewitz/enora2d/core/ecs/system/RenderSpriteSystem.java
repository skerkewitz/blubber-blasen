package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.enora2d.core.ecs.component.SpriteComponent;
import de.skerkewitz.enora2d.core.ecs.component.Transform;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.gfx.ImageData;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import de.skerkewitz.enora2d.core.gfx.Renderer;
import de.skerkewitz.enora2d.core.gfx.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class RenderSpriteSystem {

  private static final Logger logger = LogManager.getLogger(RenderSpriteSystem.class);

  private Screen screen;

  private ImageDataContainer imageDataContainer = new ImageDataContainer();

  public void update(int tickTime, Stream<Entity> stream) {
    getTuples(stream)
            .filter(tuple -> tuple.spriteComponent.renderSprite != null)
            .forEach(tuple -> renderSprite(tuple.transform, tuple.spriteComponent));
  }


  public RenderSpriteSystem(Screen screen) {
    this.screen = screen;
  }

  private void renderSprite(Transform transform, SpriteComponent sprite) {
    try {
      ImageData imageData = imageDataContainer.getResourceForName(sprite.renderSprite.namedResource);
      Renderer.renderSubImage(imageData, sprite.renderSprite.rect, sprite.colorPalette,
              screen.screenImageData, transform.position, sprite.flipX, sprite.flipY);
    } catch (IOException e) {
      logger.error("Error rendering sprite because of: " + e, e);
    }
  }

  private Stream<Tuple> getTuples(Stream<Entity> stream) {
    return stream.map(Tuple::map).filter(Objects::nonNull);
  }

  /**
   * Declares the component needed by this system.
   */
  public static class Tuple {
    public Entity entity;
    public Transform transform;
    public SpriteComponent spriteComponent;

    public Tuple(Entity entity, Transform transform, SpriteComponent spriteComponent) {
      this.entity = entity;
      this.transform = transform;
      this.spriteComponent = spriteComponent;
    }

    static Tuple map(Entity entity) {
      var sprite = entity.getComponent(SpriteComponent.class);
      var transform = entity.getComponent(Transform.class);
      if (sprite != null && transform != null) {
        return new Tuple(entity, transform, sprite);
      } else {
        return null;
      }
    }
  }
}