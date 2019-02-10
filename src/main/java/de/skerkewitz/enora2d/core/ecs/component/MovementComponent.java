package de.skerkewitz.enora2d.core.ecs.component;

import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;

public class MovementComponent implements Component {

  public int currentMovementStartTimeTick;
  public int numSteps = 0;
  public MoveableLegacyEntity.MoveDirection currentMoveDirection = MoveableLegacyEntity.MoveDirection.Idle;

  public MovementComponent(int currentMovementStartTimeTick) {
    this.currentMovementStartTimeTick = currentMovementStartTimeTick;
  }

  public void setMovementDirection(MoveableLegacyEntity.MoveDirection direction, int tickTime) {
    this.currentMovementStartTimeTick = tickTime;
    this.currentMoveDirection = direction;
    this.numSteps = 0;
  }
}
