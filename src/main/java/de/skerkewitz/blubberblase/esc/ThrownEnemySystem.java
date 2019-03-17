package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.game.world.StaticMapContent;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A common to render all SpriteComponents.
 */
public class ThrownEnemySystem extends BaseComponentSystem<ThrownEnemySystem.Tuple, ThrownEnemySystem.TupleFactory> {

  public ThrownEnemySystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    switch (t.thrownEnemyComponent.state) {
      case KICKED:
        handleThrownEnemyIfKicked(tickTime, t, world, context);
        break;
      case FALL:
        handleThrownEnemyIfFall(tickTime, t, world, context);
        break;
    }

  }

  private void handleThrownEnemyIfKicked(int tickTime, Tuple t, World world, GameContext context) {

    /* Time to switch state? */
    if (t.thrownEnemyComponent.getStateAge(tickTime) > t.thrownEnemyComponent.maxKickeTime) {
      t.thrownEnemyComponent.setState(tickTime, ThrownEnemyComponent.State.FALL);
      t.entity.addComponent(new GroundDataComponent(-4, 4, 0));
      return;
    }

    /* Update player position. */
    Point2f position = t.transformComponent.position;
    position.x += t.thrownEnemyComponent.moveVector.x * t.thrownEnemyComponent.speedIfKicked;
    position.y += t.thrownEnemyComponent.moveVector.y * t.thrownEnemyComponent.speedIfKicked;

    /* Clip it to level bounds. */
    if (position.x < 16) {
      position.x = 16;
      t.thrownEnemyComponent.moveVector.x = 1;
    } else if (position.x > StaticMapContent.WIDTH - 16) {
      position.x = StaticMapContent.WIDTH - 16;
      t.thrownEnemyComponent.moveVector.x = -1;
    }

    /* Clip it to level bounds. */
    if (position.y < 8) {
      position.y = 8;
      t.thrownEnemyComponent.moveVector.y = 1;
    } else if (position.y > StaticMapContent.HEIGHT - 8) {
      position.y = StaticMapContent.HEIGHT - 8;
      t.thrownEnemyComponent.moveVector.y = -1;
    }
  }

  private void handleThrownEnemyIfFall(int tickTime, Tuple t, World world, GameContext context) {

    /* Did we hit the ground? */
    final GroundDataComponent groundDataComponent = t.entity.getComponent(GroundDataComponent.class);
    if (groundDataComponent.isOnGround) {
      t.entity.expired();
      world.addEntity(EntityFactory.spawnDiamond(tickTime, t.transformComponent.position));
      return;
    }

    /* Update player position. */
    Point2f position = t.transformComponent.position;
    position.y += +1 * t.thrownEnemyComponent.speedIfFalling;

    LevelUtils.clipPositionToLevelBounds(position, t.entity.getComponent(BoundingBoxComponent.class).getBoundingBox());
  }


  /**
   * Declares the component needed by this common.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    ThrownEnemyComponent thrownEnemyComponent;
    TransformComponent transformComponent;

    Tuple(Entity entity, TransformComponent transformComponent, ThrownEnemyComponent thrownEnemyComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.thrownEnemyComponent = thrownEnemyComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var transformComponent = entity.getComponent(TransformComponent.class);
      var thrownEnemyComponent = entity.getComponent(ThrownEnemyComponent.class);
      if (transformComponent != null && thrownEnemyComponent != null) {
        return new Tuple(entity, transformComponent, thrownEnemyComponent);
      } else {
        return null;
      }
    }
  }
}