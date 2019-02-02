package de.skerkewitz.enora2d.common;

public class Dice {

  /**
   * Roll the dice for the given percentage.
   *
   * @param factor a percentage value where 0.0 mean 0 percent and 1.0 means 100% percent.
   * @return true if the dice rolled in favor, else false.
   */
  public static boolean chance(float factor) {
    return Math.random() < factor;
  }
}
