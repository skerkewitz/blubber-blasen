package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.Component;

/**
 * Dummy component to mark enemies.
 */
public class EnemyComponent implements Component {

  public final static int MAX_LIFETIME_IN_TICKS = 100;
  public static final int JUMP_HEIGHT_IN_PIXEL = 44;
  public static final int JUMP_HEIGHT_IN_PIXEL_GAP_JUMP = 24;

  public int jumpTickRemaining;
  public float speed = 1;
  public boolean gapJump = false;
  public boolean walkOverEdge = false;

  public boolean isAngry = false;
}
