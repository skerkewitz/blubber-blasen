package de.skerkewitz.blubberblase.esc.component;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.gfx.GdxTextureContainer;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * A system to render all SpriteComponents.
 */
public class RenderSpriteSystem extends BaseComponentSystem<RenderSpriteSystem.Tuple, RenderSpriteSystem.TupleFactory> implements RenderSystem {

  private ImageDataContainer imageDataContainer = new ImageDataContainer();
  private GdxTextureContainer textureContainer = new GdxTextureContainer();

  private SpriteBatch spriteBatch = new SpriteBatch();

  private Camera camera = null;

  public RenderSpriteSystem() {
    super(new RenderSpriteSystem.TupleFactory());
  }

  @Override
  public void willExecute(int tickTime, World world) {
    super.willExecute(tickTime, world);

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();
  }

  @Override
  public void didExecute(int tickTime, World world) {
    super.didExecute(tickTime, world);
    spriteBatch.end();
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    SpriteComponent spriteComponent = t.spriteComponent;
    Sprite sprite = textureContainer.getTextureNamedResourceAndPalette(spriteComponent.renderSprite.namedResource, spriteComponent.colorPalette, imageDataContainer);

    final Point2f pos = t.transformComponent.position.plus(spriteComponent.pivotPoint);
    sprite.setSize(spriteComponent.size.x, spriteComponent.size.y);
    sprite.setPosition(pos.x, pos.y);
    sprite.setRegion(spriteComponent.renderSprite.rect.origin.x, spriteComponent.renderSprite.rect.origin.y, spriteComponent.size.x, spriteComponent.size.y);
    sprite.setFlip(spriteComponent.flipX, !spriteComponent.flipY);
    sprite.draw(spriteBatch);
  }

  public Stream<Tuple> getTuples(Stream<Entity> stream) {
    return super.getTuples(stream)
            .filter(tuple -> tuple.spriteComponent.renderSprite != null)
            .filter(tuple -> tuple.spriteComponent.visible)
            .sorted(Comparator.comparingInt(o -> o.spriteComponent.priority));
  }

  @Override
  public void applyActiveCamera(Camera camera) {
    this.camera = camera;
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