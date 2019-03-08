package de.skerkewitz.enora2d.common;

public class Point2i {

  public int x;
  public int y;

  public final static Point2i ZERO = new Point2i(0, 0);

  public Point2i(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point2i(float x, float y) {
    this.x = (int) (x);
    this.y = (int) (y);
  }

  public Point2i(Point2i p) {
    this(p.x, p.y);
  }


  public Point2i plus(Point2i o) {
    return new Point2i(x + o.x, y + o.y);
  }

  public Point2i invert() {
    return new Point2i(-x, -y);
  }
}
