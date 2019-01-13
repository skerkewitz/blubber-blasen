package de.skerkewitz.enora2d.core.gfx;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;

public class Renderer {

  private Renderer() {
    /* Only static methods. */
  }

  public static void renderSubImage(ImageData srcImg, Rect2i srcRect, int colour, ImageData destImg, Point2i targetPos, boolean mirrorScrX, boolean mirrorScrY) {

    int scale = 1;
    int scaleMap = scale - 1;

    for (int y = 0; y < srcRect.size.height; y++) {
      int srcPixelY = srcRect.origin.y + (mirrorScrY ? srcRect.size.height - 1 - y : y);
      int destPixelY = y + targetPos.y + (y * scaleMap) - ((scaleMap << 3) / 2);

      for (int x = 0; x < srcRect.size.width; x++) {
        int srcPixelX = srcRect.origin.x + (mirrorScrX ? srcRect.size.width - 1 - x : x);
        int destPixelX = x + targetPos.x + (x * scaleMap) - ((scaleMap << 3) / 2);

        int col = (colour >> (srcImg.pixels[srcPixelX + srcPixelY * srcImg.width] * 8)) & 255;
        if (col >= 255) {
          continue;
        }

        for (int yScale = 0; yScale < scale; yScale++) {
          if (destPixelY + yScale < 0 || destPixelY + yScale >= destImg.height)
            continue;
          for (int xScale = 0; xScale < scale; xScale++) {
            if (destPixelX + xScale < 0 || destPixelX + xScale >= destImg.width)
              continue;
            destImg.pixels[(destPixelX + xScale) + (destPixelY + yScale) * destImg.width] = col;
          }
        }
      }
    }
  }
}
