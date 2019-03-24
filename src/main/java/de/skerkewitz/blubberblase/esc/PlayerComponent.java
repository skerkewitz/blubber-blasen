package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.blubberblase.entity.JumpUtils;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.ecs.Component;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;

/**
 * Dummy component to mark players.
 */
public class PlayerComponent implements Component {

  public static final int JUMP_HEIGHT_IN_PIXEL = 42;
  public static final int JUMP_HEIGHT_IN_PIXEL_MIN_JUMP = 24;

  public static final int BUBBLE_SHOOT_DELAY = TimeUtil.secondsToTickTime(0.5);

  public int speed = 1;

  final float maxVelocityX = 1.4f;
  final float accelerationX = 0.2f;

  public float velocityY = 0;
  public float velocityX = 0;

  public float gUp = JumpUtils.calcG(JUMP_HEIGHT_IN_PIXEL, maxVelocityX, 30);
  public float gDown = JumpUtils.calcG(JUMP_HEIGHT_IN_PIXEL, maxVelocityX, 30);
  public float accelerationYUp = JumpUtils.calcV0(JUMP_HEIGHT_IN_PIXEL, maxVelocityX, 30);
  public float accelerationYDown = JumpUtils.calcV0(JUMP_HEIGHT_IN_PIXEL, maxVelocityX, 10);

  /**
   * Last tick time we player spawned a bubble.
   */
  public int lastBubbleSpawnTime = 0;

  /**
   * How many more ticks can this entity stay in jump mode?
   */
  public int jumpTickRemaining = 0;

  public MoveDirection movingDir = MoveDirection.Right;
}
