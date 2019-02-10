package de.skerkewitz.enora2d.core.ecs.component;

import de.skerkewitz.enora2d.core.gfx.RenderSprite;

public class SpriteComponent implements Component {
  public RenderSprite renderSprite = null;
  public int colorPalette = 0;

  public boolean flipX = false;
  public boolean flipY = false;

}
