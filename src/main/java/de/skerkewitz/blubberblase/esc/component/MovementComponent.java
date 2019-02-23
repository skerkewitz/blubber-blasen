package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;

public class MovementComponent implements Component {

  public int currentMovementStartTimeTick;
  public int numSteps = 0;
  public MoveableLegacyEntity.MoveDirection currentMoveDirection;

  public MovementComponent(int tickTime) {
    this(tickTime, MoveableLegacyEntity.MoveDirection.Idle);
  }

  public MovementComponent(int tickTime, MoveableLegacyEntity.MoveDirection currentMoveDirection) {
    this.currentMovementStartTimeTick = tickTime;
    this.currentMoveDirection = currentMoveDirection;
  }

  public void setMovementDirection(MoveableLegacyEntity.MoveDirection direction, int tickTime) {
    this.currentMovementStartTimeTick = tickTime;
    this.currentMoveDirection = direction;
    this.numSteps = 0;
  }
}
