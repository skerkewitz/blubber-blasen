package de.skerkewitz.enora2d.core.gfx;

public class ImageData {

  public final int width;
  public final int height;
  public int[] pixels;


  public ImageData(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
  }
}
