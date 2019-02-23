package de.skerkewitz.blubberblase.esc.systems;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.skerkewitz.blubberblase.esc.component.SpriteComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.World;
import de.skerkewitz.enora2d.core.gfx.GdxTextureContainer;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class RenderSpriteSystem extends BaseComponentSystem<RenderSpriteSystem.Tuple, RenderSpriteSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(RenderSpriteSystem.class);

  private ImageDataContainer imageDataContainer = new ImageDataContainer();
  private GdxTextureContainer textureContainer = new GdxTextureContainer();

  private SpriteBatch spriteBatch = new SpriteBatch();

  public RenderSpriteSystem() {
    super(new RenderSpriteSystem.TupleFactory());
  }

  @Override
  public void willExecute(int tickTime, World world) {
    super.willExecute(tickTime, world);

    spriteBatch.setProjectionMatrix(world.projectionMatrix);
    spriteBatch.begin();
  }

  @Override
  public void didExecute(int tickTime, World world) {
    super.didExecute(tickTime, world);
    spriteBatch.end();
  }

  @Override
  public void execute(int tickTime, Tuple tuple, World world) {
    TransformComponent transformComponent = tuple.transformComponent;
    SpriteComponent spriteComponent = tuple.spriteComponent;
    try {
      Sprite sprite = textureContainer.getTextureNamedResourceAndPalette(spriteComponent.renderSprite.namedResource, spriteComponent.colorPalette, imageDataContainer);

      Point2i pos = transformComponent.position.plus(spriteComponent.pivotPoint);

      sprite.setSize(16, 16);
      sprite.setPosition(pos.x, pos.y);
      sprite.setRegion(spriteComponent.renderSprite.rect.origin.x, spriteComponent.renderSprite.rect.origin.y, 16, 16);
      sprite.setFlip(spriteComponent.flipX, !spriteComponent.flipY);
      sprite.draw(spriteBatch);

      //      Renderer.renderSubImage(imageData, sprite.renderSprite.rect, sprite.colorPalette,
//              screen.screenImageData, transformComponent.position.plus(sprite.pivotPoint), sprite.flipX, sprite.flipY);
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