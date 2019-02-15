package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.component.Component;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;

public class SpriteComponent implements Component {

  /**
   * How much we need to move the upper left corner of the sprite relative to the transform position.
   */
  public Point2i pivotPoint = Point2i.ZERO;

  public RenderSprite renderSprite = null;
  public int colorPalette = 0;

  public boolean flipX = false;
  public boolean flipY = false;

}
