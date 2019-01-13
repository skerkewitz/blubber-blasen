package de.skerkewitz.enora2d.core.gfx;

import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;

public class Screen {

  public static final int MAP_WIDTH = 64;
  public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

  public static final byte BIT_MIRROR_X = 0x01;
  public static final byte BIT_MIRROR_Y = 0x02;

  private int xOffset = 0;
  private int yOffset = 0;

  public final ImageData imageData;

  public ImageData sheet;

  public Screen(int pixelWidth, int pixelHeight, ImageData sheet) {
    this.imageData = new ImageData(pixelWidth, pixelHeight, new int[pixelWidth * pixelHeight]);
    this.sheet = sheet;
  }

  public void render(int xPos, int yPos, int tile, int colour, int mirrorDir, int scale) {
    xPos -= xOffset;
    yPos -= yOffset;

    boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
    boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

    int scaleMap = scale - 1;
    int xTile = tile % 32;
    int yTile = tile / 32;

    Renderer.renderSubImage(sheet, new Rect2i(xTile * 8, yTile * 8, 8, 8), colour, imageData, new Point2i(xPos, yPos), mirrorX, mirrorY);
  }

  public void setOffset(int xOffset, int yOffset) {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  public static class Sprite {
    public int tileIdx;
    public SpriteSheet sheet;

    public Sprite(int tileIdx, SpriteSheet sheet) {
      this.tileIdx = tileIdx;
      this.sheet = sheet;
    }
  }
}
