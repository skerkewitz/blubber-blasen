package de.skerkewitz.enora2d.core.gfx;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.common.Size2i;

public class SpriteSheet {

  private final Size2i inset;
  private final Size2i marging;
  private final Size2i size;

  public SpriteSheet(Size2i inset, Size2i marging, Size2i size) {
    this.inset = inset;
    this.marging = marging;
    this.size = size;
  }

  public int xForIndex(int xIndex) {
    return inset.width + ((marging.width + size.width) * xIndex);
  }

  public int yForIndex(int yIndex) {
    return inset.height + ((marging.height + size.height) * yIndex);
  }

  public Rect2i rectFor(int xIndex, int yIndex) {
    return new Rect2i(new Point2i(xForIndex(xIndex), yForIndex(yIndex)), new Size2i(size.width, size.height));
  }
}
