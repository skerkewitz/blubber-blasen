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
import de.skerkewitz.enora2d.core.gfx.NamedResource;

import java.util.Comparator;

/**
 * A system to render all {@link RenderTextComponent}.
 */
public class RenderTextSystem extends BaseComponentSystem<RenderTextSystem.Tuple, RenderTextSystem.TupleFactory> implements RenderSystem {

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    RenderTextComponent renderTextComponent;

    Tuple(Entity entity, TransformComponent transformComponent, RenderTextComponent renderTextComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.renderTextComponent = renderTextComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var sprite = entity.getComponent(RenderTextComponent.class);
      var transform = entity.getComponent(TransformComponent.class);
      if (sprite != null && transform != null) {
        return new Tuple(entity, transform, sprite);
      } else {
        return null;
      }
    }
  }

  private static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,:;'\"!?$%()-=+/      ";
  private GdxTextureContainer textureContainer = new GdxTextureContainer();
  private SpriteBatch spriteBatch = new SpriteBatch();
  private Camera camera = null;

  public RenderTextSystem(Vector3 zero) {
    super(new RenderTextSystem.TupleFactory());
    this.componentPredicate = tuple -> tuple.renderTextComponent.spriteSource != null && tuple.renderTextComponent.isVisible();
    this.componentComparator = Comparator.comparingInt(o -> o.renderTextComponent.getPriority());
  }

  @Override
  public void willExecute(int tickTime, World world) {
    super.willExecute(tickTime, world);

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();
  }

  @Override
  public void didExecute(int tickTime, World world, boolean didProcessAnything) {
    super.didExecute(tickTime, world, didProcessAnything);
    spriteBatch.end();
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    var text = t.renderTextComponent.text.toUpperCase();

    for (int i = 0; i < text.length(); i += 1) {
      int charIndex = chars.indexOf(text.charAt(i));
      if (charIndex >= 0) {

        var xOffset = (charIndex % 32) * 8;
        var yOffset = (charIndex / 32) * 8;

        final RenderTextComponent renderTextComponent = t.renderTextComponent;

        final Sprite sprite;
        final NamedResource namedResource = renderTextComponent.spriteSource.namedResource;
        if (namedResource.directColor) {
          sprite = textureContainer.getTextureNamedResource(namedResource);
        } else {
          throw new UnsupportedOperationException("Only direct color is supported!");
        }

        final Point2f pos = t.transformComponent.position.plus(renderTextComponent.pivotPoint);
        sprite.setSize(8, 8);
        sprite.setPosition(pos.x + (i * 8), pos.y);
        sprite.setRegion(renderTextComponent.spriteSource.rect.origin.x + xOffset, renderTextComponent.spriteSource.rect.origin.y + yOffset, 8, 8);
        sprite.setFlip(false, true);
        sprite.setColor(t.renderTextComponent.color);
        sprite.draw(spriteBatch, renderTextComponent.getAlpha());

      }

    }

  }

  @Override
  public void applyActiveCamera(Camera camera) {
    this.camera = camera;
  }
}