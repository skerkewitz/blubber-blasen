package de.skerkewitz.enora2d.core.ecs.system;

import de.gierzahn.editor.map.AirflowDirection;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.esc.MovementComponent;
import de.skerkewitz.blubberblase.esc.StateBaseBubbleComponent;
import de.skerkewitz.blubberblase.esc.TransformComponent;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
 */
public class AirflowSystem extends BaseComponentSystem<AirflowSystem.Tuple, AirflowSystem.TupleFactory> {

  /**
   * Declares the component needed by this system.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    TransformComponent transformComponent;
    MovementComponent movementComponent;
    StateBaseBubbleComponent stateBaseBubbleComponent;

    Tuple(Entity entity, TransformComponent transformComponent, MovementComponent movementComponent, StateBaseBubbleComponent stateBaseBubbleComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.movementComponent = movementComponent;
      this.stateBaseBubbleComponent = stateBaseBubbleComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<AirflowSystem.Tuple> {

    public AirflowSystem.Tuple map(Entity entity) {
      var transformComponent = entity.getComponent(TransformComponent.class);
      var movementComponent = entity.getComponent(MovementComponent.class);
      var stateBaseBubbleComponent = entity.getComponent(StateBaseBubbleComponent.class);
      if (transformComponent != null && movementComponent != null && stateBaseBubbleComponent != null) {
        return new AirflowSystem.Tuple(entity, transformComponent, movementComponent, stateBaseBubbleComponent);
      }
      return null;
    }
  }

  public AirflowSystem() {
    super(new AirflowSystem.TupleFactory());
  }

  @Override
  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    if (t.stateBaseBubbleComponent.state == StateBaseBubbleComponent.State.FLOAT) {

      if (tickTime % 10 == 0) {
        AirflowDirection airflowDirection = world.getAirflowAt(t.transformComponent.position);

        if (airflowDirection == AirflowDirection.Empty) {
          int value = (int) (Math.random() * 4) + 1;
          airflowDirection = AirflowDirection.fromCode(value);
        }

        switch (airflowDirection) {
          case Empty:
            t.movementComponent.setMovementDirection(MoveDirection.Idle, tickTime);
            break;
          case UP:
            t.movementComponent.setMovementDirection(MoveDirection.Up, tickTime);
            break;
          case DOWN:
            t.movementComponent.setMovementDirection(MoveDirection.Down, tickTime);
            break;
          case LEFT:
            t.movementComponent.setMovementDirection(MoveDirection.Left, tickTime);
            break;
          case RIGHT:
            t.movementComponent.setMovementDirection(MoveDirection.Right, tickTime);
            break;
        }
      }
    }

    switch (t.movementComponent.currentMoveDirection) {
      case Up:
        move(0, -1, t.transformComponent, t.movementComponent);
        break;
      case Left:
        move(-1, 0, t.transformComponent, t.movementComponent);
        break;
      case Right:
        move(+1, 0, t.transformComponent, t.movementComponent);
        break;
      case Down:
        move(0, 1, t.transformComponent, t.movementComponent);
        break;
      case Idle:
        break;
    }
  }

  /**
   * Move the entity the given amount in the width and height direction.
   * <p>
   * This method will call hasCollided() internaly to check the entity movement again the world.
   *
   * @param xa
   * @param ya
   */
  void move(int xa, int ya, TransformComponent targetTransformComponent, MovementComponent movementComponent) {

    movementComponent.numSteps++;

    /* Update player position. */
    final Point2f position = targetTransformComponent.position;
    position.x += xa * movementComponent.speed;
    position.y += ya * movementComponent.speed;
  }

  AirflowDirection moveDirectionToAirflow(MoveDirection moveDirection) {
    switch (moveDirection) {
      case Idle:
        return AirflowDirection.Empty;
      case Up:
        return AirflowDirection.UP;
      case Down:
        return AirflowDirection.DOWN;
      case Left:
        return AirflowDirection.LEFT;
      case Right:
        return AirflowDirection.RIGHT;
    }

    return AirflowDirection.Empty;
  }
}