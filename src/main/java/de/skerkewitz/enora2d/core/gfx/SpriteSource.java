package de.skerkewitz.enora2d.core.gfx;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;

public class SpriteSource {

  /**
   * The position, weidht and height in source image data.
   */
  public final Rect2i rect;

  /**
   * The actual image data.
   */
  public final NamedResource namedResource;

  public SpriteSource(Rect2i rect, NamedResource namedResource) {
    this.rect = rect;
    this.namedResource = namedResource;
  }

  public SpriteSource(Point2i spriteIndex, NamedResource namedResource) {
    this.namedResource = namedResource;
    this.rect = this.namedResource.sheet.rectFor(spriteIndex.x, spriteIndex.y);
  }
}
