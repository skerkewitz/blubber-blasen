package de.skerkewitz.enora2d.core.gfx;

import de.skerkewitz.enora2d.common.Rect2i;

public class RenderSprite {

  /**
   * The position, weidht and height in source image data.
   */
  public final Rect2i rect;

  /**
   * The actual image data.
   */
  public final NamedResource namedResource;

  public RenderSprite(Rect2i rect, NamedResource namedResource) {
    this.rect = rect;
    this.namedResource = namedResource;
  }
}
