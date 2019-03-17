package de.skerkewitz.enora2d.common;

public class Point2f {

  public final static Point2f ZERO = new Point2f(0, 0);
  public float x;
  public float y;

  public Point2f(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Point2f(Point2f p) {
    this(p.x, p.y);
  }


  public Point2f plus(Point2f o) {
    return plus(o.x, o.y);
  }

  public Point2f plus(float x, float y) {
    return new Point2f(this.x + x, this.y + y);
  }

  public Point2f plus(Point2i o) {
    return new Point2f(x + o.x, y + o.y);
  }

  public Point2i toPoint2i() {
    return new Point2i((int) x, (int) y);
  }


  public Point2f cloneCopy() {
    return new Point2f(this);
  }
}
