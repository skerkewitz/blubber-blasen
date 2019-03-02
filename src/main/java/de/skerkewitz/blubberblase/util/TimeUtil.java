package de.skerkewitz.blubberblase.util;

public class TimeUtil {
  public static final double TARGET_FPS = 60D;
  public static final int TICKTIME_1s = secondsToTickTime(1);
  public static final int TICKTIME_2s = secondsToTickTime(2);
  public static final int TICKTIME_5s = secondsToTickTime(5);


  private TimeUtil() {
    /* No instance allowed. */
  }

  public static int secondsToTickTime(double seconds) {
    return (int) (seconds * TARGET_FPS);
  }

  public static int randomSecondsToTickTime(double seconds) {
    return (int) ((seconds * TARGET_FPS) * Math.random());
  }
}
