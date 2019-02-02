package de.skerkewitz.enora2d.core.entity;

import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.game.level.Level;

public abstract class MoveableEntity extends AbstractEntity {

  protected final Rect2i boundingBox;

  /**
   * How many more ticks can this entity stay in jump mode?
   */
  protected int jumpTickRemaining = 0;

  /**
   * Move the entity the given amount in the width and height direction.
   * <p>
   * This method will call hasCollided() internaly to check the entity movement again the world.
   *
   * @param level
   * @param xa
   * @param ya
   */
  protected boolean move(Level level, int xa, int ya) {

    /* Handle diagonal movement. */
    if (xa != 0 && ya != 0) {
      var seconds = move(level, 0, ya);
      var first = move(level, xa, 0);
      if (first && seconds) {
        numSteps--;
      }
      return first || seconds;
    }

    /* If there is a collision then we will no update the player pos. */
    if (ya >= 0 && hasCollided(level, xa, ya)) {
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
    posX += xa * speed;
    posY += ya * speed;

    return true;
  }

  protected String name;
  protected int speed;
  protected int numSteps = 0;
  protected boolean isMoving;
  protected MoveDirection movingDir = MoveDirection.Up;

  public MoveableEntity(String name, int x, int y, int speed, Rect2i bbox) {
    super();
    this.name = name;
    this.posX = x;
    this.posY = y;
    this.speed = speed;
    this.boundingBox = bbox;
  }

  public boolean hasCollided(Level level, int xa, int ya) {

    int xMin = boundingBox.origin.x;
    int xMax = boundingBox.size.width;
    int yMin = boundingBox.origin.y;
    int yMax = boundingBox.size.width;

    if (level.isSolidTile(this.posX, this.posY, xa, ya, xMin, yMin)) {
      return true;
    }

    if (level.isSolidTile(this.posX, this.posY, xa, ya, xMax, yMin)) {
      return true;
    }

    if (level.isSolidTile(this.posX, this.posY, xa, ya, xMin, yMax)) {
      return true;
    }

    return level.isSolidTile(this.posX, this.posY, xa, ya, xMax, yMax);

  }

  /**
   * True if the object is standing on ground.
   *
   * @param level
   * @return
   */
  protected boolean isOnGround(Level level) {
    return hasCollided(level, 0, +1);
  }

  public enum MoveDirection {
    Up, Down, Left, Right;

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
