package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.blubberblase.esc.component.TransformComponent;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.game.world.World;

public abstract class MoveableLegacyEntity extends AbstractLegacyEntity {

  /**
   * How many more ticks can this entity stay in jump mode?
   */
  protected int jumpTickRemaining = 0;

  public MoveableLegacyEntity(String name, int speed) {
    super();
    this.name = name;
    this.speed = speed;
  }

  protected String name;
  protected int speed;
  protected int numSteps = 0;
  protected boolean isMoving;
  protected MoveDirection movingDir = MoveDirection.Up;

  /**
   * Move the entity the given amount in the width and height direction.
   * <p>
   * This method will call hasCollided() internaly to check the entity movement again the world.
   *
   * @param world
   * @param xa
   * @param ya
   */
  protected boolean move(World world, int xa, int ya) {

    /* Handle diagonal movement. */
    if (xa != 0 && ya != 0) {
      var seconds = move(world, 0, ya);
      var first = move(world, xa, 0);
      if (first && seconds) {
        numSteps--;
      }
      return first || seconds;
    }

    /* If there is a collision then we will no update the player pos. */
    if (ya >= 0 && LevelUtils.hasCollided(this, world, xa, ya)) {
      return false;
    }

    numSteps++;

    if (ya < 0)
      movingDir = MoveDirection.Up;
    if (ya > 0)
      movingDir = MoveDirection.Down;
    if (xa < 0)
      movingDir = MoveDirection.Left;
    if (xa > 0)
      movingDir = MoveDirection.Right;

    /* Update player position. */
    Point2i position = getComponent(TransformComponent.class).position;
    position.x += xa * speed;
    position.y += ya * speed;

    return true;
  }

  public enum MoveDirection {
    Idle, Up, Down, Left, Right;

    public static MoveDirection parseFromInt(int intValue) {
      for (MoveDirection md : values()) {
        if (intValue == md.ordinal()) {
          return md;
        }
      }

      throw new IllegalArgumentException("Could not find a MoveDirection with int value " + intValue);
    }
  }


  public String getName() {
    return name;
  }

  public int getNumSteps() {
    return numSteps;
  }

  public void setNumSteps(int numSteps) {
    this.numSteps = numSteps;
  }

  public boolean isMoving() {
    return isMoving;
  }

  public void setMoving(boolean isMoving) {
    this.isMoving = isMoving;
  }

  public MoveDirection getMovingDir() {
    return movingDir;
  }

  public void setMovingDir(MoveDirection movingDir) {
    this.movingDir = movingDir;
  }

}
