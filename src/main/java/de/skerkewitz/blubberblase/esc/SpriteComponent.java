package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.Component;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;

public class SpriteComponent implements Component {

  /**
   * How much we need to move the upper left corner of the sprite relative to the transform position.
   */
  public Point2i pivotPoint = Point2i.ZERO;
  /**
   * The actual sprite to render.
   */
  public RenderSprite renderSprite = null;
  /** The 4 color palette for this sprite. */
  public int colorPalette = 0;
  /**
   * Render priority, lower get rendered first therefore higher sprites are in front.
   */
  public byte priority = 0;

  public boolean flipX = false;
  public boolean flipY = false;
  public boolean visible = true;
  public Size size = Size.Big;

  enum Size {
    Small(8, 8),
    Big(16, 16);

    public final int x;
    public final int y;

    Size(int x, int y) {

      this.x = x;
      this.y = y;
    }
  }

  public float alpha = 1.0f;

  //  class ObjectAttributes {
//
//    int size = 1; // 1 means 8x8, 2 means 16x16
//    //int tileIndex;
//
//  }
//
//  // Size (8x8, 16x16), pos, prio, flipv, fliph, first tile, color palette



}
