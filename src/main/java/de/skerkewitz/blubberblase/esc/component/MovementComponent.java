package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;

public class MovementComponent implements Component {

  public int speed = 1;
  public int currentMovementStartTimeTick;
  public int numSteps = 0;
  public MoveableLegacyEntity.MoveDirection currentMoveDirection;

  public MovementComponent(int tickTime) {
    this(tickTime, MoveableLegacyEntity.MoveDirection.Idle, 1);
  }

  public MovementComponent(int tickTime, MoveableLegacyEntity.MoveDirection currentMoveDirection, int speed) {
    this.currentMovementStartTimeTick = tickTime;
    this.currentMoveDirection = currentMoveDirection;
    this.speed = speed;
  }

  public void setMovementDirection(MoveableLegacyEntity.MoveDirection direction, int tickTime) {
    this.currentMovementStartTimeTick = tickTime;
    this.currentMoveDirection = direction;
    this.numSteps = 0;
  }
}
