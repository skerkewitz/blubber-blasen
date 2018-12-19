package de.skerkewitz.enora2d.common;

public class Point2i {

  public final int x;
  public final int y;

  public final static Point2i ZERO = new Point2i(0,0);

  public Point2i(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
