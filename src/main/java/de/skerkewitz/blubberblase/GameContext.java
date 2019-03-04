package de.skerkewitz.blubberblase;

/**
 * Contains game related state that is no entity based.
 */
public class GameContext {
  public static final int MAX_LEVEL = 5;
  public int currentLevelNum = 1;

  public int isLevelClearedTimer = -1;
  public boolean gameOver = false;

  public void clampLevelNum() {
    currentLevelNum = Math.max(1, currentLevelNum % (GameContext.MAX_LEVEL + 1));
  }
}
