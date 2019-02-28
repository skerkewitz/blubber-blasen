package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.common.TimeUtil;
import de.skerkewitz.enora2d.core.ecs.component.Component;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;

/**
 * Dummy component to mark players.
 */
public class PlayerComponent implements Component {

  public static final int JUMP_HEIGHT_IN_PIXEL = 44;

  public static final int BUBBLE_SHOOT_DELAY = TimeUtil.secondsToTickTime(0.5);

  public int speed = 1;

  /**
   * Last tick time we player spawned a bubble.
   */
  public int lastBubbleSpawnTime = 0;

  /**
   * How many more ticks can this entity stay in jump mode?
   */
  public int jumpTickRemaining = 0;

  public MoveableLegacyEntity.MoveDirection movingDir = MoveableLegacyEntity.MoveDirection.Up;

}
