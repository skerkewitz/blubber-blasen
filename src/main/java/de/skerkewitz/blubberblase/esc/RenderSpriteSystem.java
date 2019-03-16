package de.skerkewitz.blubberblase.esc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.gfx.GdxTextureContainer;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import de.skerkewitz.enora2d.core.gfx.NamedResource;

import java.util.Comparator;

/**
 * A system to render all SpriteComponents.
 */
public class RenderSpriteSystem extends BaseComponentSystem<RenderSpriteSystem.Tuple, RenderSpriteSystem.TupleFactory> implements RenderSystem {

  private ImageDataContainer imageDataContainer = new ImageDataContainer();
  private GdxTextureContainer textureContainer = new GdxTextureContainer();

  private SpriteBatch spriteBatch = new SpriteBatch();

  private Camera camera = null;

  private Vector3 translate;

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    RenderSpriteComponent renderSpriteComponent;

    Tuple(Entity entity, TransformComponent transformComponent, RenderSpriteComponent renderSpriteComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.renderSpriteComponent = renderSpriteComponent;
    }
  }

  public RenderSpriteSystem(Vector3 translate) {
    super(new RenderSpriteSystem.TupleFactory());
    this.translate = translate;
    this.componentPredicate = tuple -> tuple.renderSpriteComponent.spriteSource != null && tuple.renderSpriteComponent.isVisible();
    this.componentComparator = Comparator.comparingInt(o -> o.renderSpriteComponent.getPriority());
  }

  @Override
  public void didExecute(int tickTime, World world, boolean didProcessAnything) {
    super.didExecute(tickTime, world, didProcessAnything);
    spriteBatch.end();
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var sprite = entity.getComponent(RenderSpriteComponent.class);
      var transform = entity.getComponent(TransformComponent.class);
      if (sprite != null && transform != null) {
        return new Tuple(entity, transform, sprite);
      } else {
        return null;
      }
    }
  }

  @Override
  public void applyActiveCamera(Camera camera) {
    this.camera = camera;
  }

  @Override
  public void willExecute(int tickTime, World world) {
    super.willExecute(tickTime, world);


    spriteBatch.setProjectionMatrix(camera.combined.cpy().translate(translate));
    spriteBatch.begin();
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    final RenderSpriteComponent renderSpriteComponent = t.renderSpriteComponent;

    final Sprite sprite;
    final NamedResource namedResource = renderSpriteComponent.spriteSource.namedResource;
    if (namedResource.directColor) {
      sprite = textureContainer.getTextureNamedResource(namedResource);
    } else {
      sprite = textureContainer.getTextureNamedResourceAndPalette(namedResource, renderSpriteComponent.colorPalette, imageDataContainer);
    }

    final Point2f pos = t.transformComponent.position.plus(renderSpriteComponent.pivotPoint);
    sprite.setSize(renderSpriteComponent.size.x, renderSpriteComponent.size.y);
    sprite.setPosition(pos.x, pos.y);
    sprite.setRegion(renderSpriteComponent.spriteSource.rect.origin.x, renderSpriteComponent.spriteSource.rect.origin.y, renderSpriteComponent.size.x, renderSpriteComponent.size.y);
    sprite.setFlip(renderSpriteComponent.flipX, !renderSpriteComponent.flipY);
    sprite.draw(spriteBatch, renderSpriteComponent.getAlpha());
  }
}