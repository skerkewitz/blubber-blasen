package de.skerkewitz.enora2d.core.entity;

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
