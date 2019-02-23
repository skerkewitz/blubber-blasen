package de.skerkewitz.enora2d.core.game;

public class TimeUtil {
  public static final double TARGET_FPS = 60D;
  public static final int TICKTIME_1s = secondsToTickTime(1);
  public static final int TICKTIME_5s = secondsToTickTime(5);

  private TimeUtil() {
    /* No instance allowed. */
  }

  public static int secondsToTickTime(double seconds) {
    return (int) (seconds * TARGET_FPS);
  }
}
