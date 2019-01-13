package de.skerkewitz.enora2d.common;

public class Rect2i {

  /**
   * A point that specifies the coordinates of the rectangle’s origin.
   */
  public final Point2i origin;

  /**
   * A size that specifies the height and width of the rectangle.
   */
  public final Size2i size;

  public Rect2i(Point2i origin, Size2i size2i) {
    this.origin = origin;
    this.size = size2i;
  }

  public Rect2i(int x, int y, int width, int height) {
    this.origin = new Point2i(x, y);
    this.size = new Size2i(width, height);
  }

}
