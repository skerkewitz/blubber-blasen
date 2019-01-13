package de.skerkewitz.enora2d.core.gfx;

import de.skerkewitz.enora2d.common.Size2i;

import java.io.IOException;

public class SpriteSheet {

  public final ImageData imageData;

  public final Size2i inset;


  public SpriteSheet() throws IOException {
    imageData = new ImageData("/Enemies.png");
    inset = new Size2i(5, 5);
  }
}
