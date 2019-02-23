package de.skerkewitz.enora2d.core.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageData {

  public final int width;
  public final int height;
  public int[] pixels;

  public ImageData(String path) throws IOException {
    BufferedImage image = ImageIO.read(ImageData.class.getResourceAsStream(path));

    this.width = image.getWidth();
    this.height = image.getHeight();

    pixels = image.getRGB(0, 0, width, height, null, 0, width);


    for (int i = 0; i < pixels.length; i++) {
      /* We only care about four different values so we can flatten them. */
      pixels[i] = (pixels[i] & 0xff) / 64;
    }
  }

  public ImageData(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
  }

}
