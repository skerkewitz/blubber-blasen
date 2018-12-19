package de.skerkewitz.enora2d.common;

public class Size2i {

  public final int width;
  public final int height;

  public final static Size2i ZERO = new Size2i(0, 0);

  public Size2i(int width, int height) {
    this.width = width;
    this.height = height;
  }
}
