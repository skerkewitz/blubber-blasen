package de.skerkewitz.blubberblase.esc.component;


import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.Component;

public class ThrownEnemyComponent extends StateBaseComponent<ThrownEnemyComponent.State> implements Component {

  public final float speedIfKicked = 2.5f;
  public final float speedIfFalling = 1.0f;
  public final Point2i moveVector;

  public final int maxKickeTime = ThrownEnemy.KICKED_TIME + TimeUtil.randomSecondsToTickTime(1.0f);

  public ThrownEnemyComponent(int stateBeginFrameCount, Point2i moveVector) {
    super(stateBeginFrameCount, State.KICKED);
    this.moveVector = moveVector;
  }

  enum State {
    KICKED,
    FALL
  }
}
