package de.skerkewitz.enora2d.core.ecs;

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

  public MoveDirection flipHorizontal() {
    if (this == MoveDirection.Left) {
      return MoveDirection.Right;
    } else if (this == MoveDirection.Right) {
      return MoveDirection.Left;
    }

    /* Don't change. */
    return this;
  }

  public int getHorizontalMoveVector() {

    if (this == MoveDirection.Left) {
      return -1;
    } else if (this == MoveDirection.Right) {
      return +1;
    }

    /* Don't change. */
    return 0;
  }
}
