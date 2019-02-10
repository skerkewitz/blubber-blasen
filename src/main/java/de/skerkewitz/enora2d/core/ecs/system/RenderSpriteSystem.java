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

/**
 * A system to render all SpriteComponents.
 */
public class RenderSpriteSystem {

  private static final Logger logger = LogManager.getLogger(RenderSpriteSystem.class);

  private Screen screen;

  private ImageDataContainer imageDataContainer = new ImageDataContainer();

  public RenderSpriteSystem(Screen screen) {
    this.screen = screen;
  }

  public void update(int tickTime, Iterable<Entity> iterator) {
    iterator.forEach((Entity e) -> {

      /* If we have a sprite component then render it. */
      var sprite = e.getComponent(SpriteComponent.class);
      var transform = e.getComponent(Transform.class);
      if (sprite != null && transform != null) {
        renderSprite(transform, sprite);
      }
    });
  }

  private void renderSprite(Transform transform, SpriteComponent sprite) {
    if (sprite.renderSprite == null) {
      logger.warn("Entity found without valid renderSprite, ignoring...");
    } else {
      try {
        ImageData imageData = imageDataContainer.getResourceForName(sprite.renderSprite.namedResource);
        Renderer.renderSubImage(imageData, sprite.renderSprite.rect, sprite.colorPalette,
                screen.screenImageData, transform.position, sprite.flipX, sprite.flipY);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
