package de.skerkewitz.blubberblase;

/**
 * Contains game related state that is no entity based.
 */
public class GameContext {
  public static final int MAX_LEVEL = 10;
  public int currentLevelNum = 1;

  public int isLevelClearedTimer = -1;

  private boolean gameOver = false;
  private int gameOverTickTime = 0;

  public int scorePlayer1 = 0;

  public void clampLevelNum() {
    currentLevelNum = Math.max(1, currentLevelNum % (GameContext.MAX_LEVEL + 1));
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public void setGameOver(int tickTime) {
    gameOver = true;
    gameOverTickTime = tickTime;
  }

  public int getGameOverTickTime() {
    return gameOverTickTime;
  }
}
