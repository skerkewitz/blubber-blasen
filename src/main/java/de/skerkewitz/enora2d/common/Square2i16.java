package de.skerkewitz.enora2d.common;

public class Square2i16 extends Rect2i {

  /**
   * A 16 by 16 size.
   */
  private final static Size2i size16x16 = new Size2i(16, 16);

  public Square2i16(Point2i origin) {
    super(origin, size16x16);
  }

  public Square2i16(int x, int y) {
    this(new Point2i(x, y));
  }

}
