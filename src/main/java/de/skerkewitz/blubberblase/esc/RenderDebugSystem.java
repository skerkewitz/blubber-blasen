package de.skerkewitz.blubberblase.esc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
 */
public class RenderDebugSystem extends BaseComponentSystem<RenderDebugSystem.Tuple, RenderDebugSystem.TupleFactory> implements RenderSystem {

  private ShapeRenderer shapeRenderer = new ShapeRenderer();
  private Camera camera = null;

  public RenderDebugSystem() {
    super(new RenderDebugSystem.TupleFactory());
  }

  @Override
  public void applyActiveCamera(Camera camera) {
    this.camera = camera;
  }

  @Override
  public void willExecute(int tickTime, World world) {
    super.willExecute(tickTime, world);

    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setProjectionMatrix(camera.combined);
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    final BoundingBoxComponent boundingBoxComponent = t.boundingBoxComponent;
    final Rect2i boundingBox = new Rect2i(t.transformComponent.position.toPoint2i().plus(boundingBoxComponent.getBoundingBox().origin), boundingBoxComponent.getBoundingBox().size);
    shapeRenderer.setColor(t.collisionComponent.hasCollission() ? Color.RED : Color.GREEN);
    shapeRenderer.rect(boundingBox.origin.x, boundingBox.origin.y, boundingBox.size.width, boundingBox.size.height);
  }

  @Override
  public void didExecute(int tickTime, World world, boolean didProcessAnything) {
    super.didExecute(tickTime, world, didProcessAnything);
    shapeRenderer.end();
  }

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    BoundingBoxComponent boundingBoxComponent;
    CollisionComponent collisionComponent;

    Tuple(Entity entity, TransformComponent transformComponent, BoundingBoxComponent boundingBoxComponent, CollisionComponent collisionComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.boundingBoxComponent = boundingBoxComponent;
      this.collisionComponent = collisionComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {
    public Tuple map(Entity entity) {
      var transform = entity.getComponent(TransformComponent.class);
      var boundingBoxComponent = entity.getComponent(BoundingBoxComponent.class);
      var collisionComponent = entity.getComponent(CollisionComponent.class);
      if (boundingBoxComponent != null && transform != null && collisionComponent != null) {
        return new Tuple(entity, transform, boundingBoxComponent, collisionComponent);
      } else {
        return null;
      }
    }
  }
}