package de.skerkewitz.blubberblase.esc.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.skerkewitz.blubberblase.esc.component.BoundingBoxComponent;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.ecs.system.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.system.ComponentSystem;
import de.skerkewitz.enora2d.core.game.level.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A system to render all SpriteComponents.
 */
public class RenderDebugSystem extends BaseComponentSystem<RenderDebugSystem.Tuple, RenderDebugSystem.TupleFactory> {

  private static final Logger logger = LogManager.getLogger(RenderDebugSystem.class);

  private ShapeRenderer shapeRenderer = new ShapeRenderer();
  private Camera camera;

  public RenderDebugSystem(Camera camera) {
    super(new RenderDebugSystem.TupleFactory());
    this.camera = camera;
  }

  @Override
  public void willExecute(int tickTime, World world) {
    super.willExecute(tickTime, world);

    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setProjectionMatrix(camera.combined);
  }

  @Override
  public void execute(int tickTime, Tuple t, World world) {

    final BoundingBoxComponent boundingBoxComponent = t.boundingBoxComponent;
    final Rect2i boundingBox = new Rect2i(t.transformComponent.position.plus(boundingBoxComponent.getBoundingBox().origin), boundingBoxComponent.getBoundingBox().size);
    shapeRenderer.setColor(Color.WHITE);
    shapeRenderer.rect(boundingBox.origin.x, boundingBox.origin.y, boundingBox.size.width, boundingBox.size.height);
  }

  @Override
  public void didExecute(int tickTime, World world) {
    super.didExecute(tickTime, world);
    shapeRenderer.end();
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