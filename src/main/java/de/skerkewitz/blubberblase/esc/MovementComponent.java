package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.enora2d.core.ecs.Component;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;

public class MovementComponent implements Component {

  public float speed = 1.0f;
  public int currentMovementStartTimeTick;
  public int numSteps = 0;
  public MoveDirection currentMoveDirection;

  public MovementComponent(int tickTime) {
    this(tickTime, MoveDirection.Idle, 1);
  }

  public MovementComponent(int tickTime, MoveDirection currentMoveDirection, float speed) {
    this.currentMovementStartTimeTick = tickTime;
    this.currentMoveDirection = currentMoveDirection;
    this.speed = speed;
  }

  public void setMovementDirection(MoveDirection direction, int tickTime) {
    this.currentMovementStartTimeTick = tickTime;
    this.currentMoveDirection = direction;
    this.numSteps = 0;
  }
}
