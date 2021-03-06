package de.skerkewitz.enora2d.core.gfx;

public class RgbColorPalette {

  public static final int GREEN = 050;
  public static final int WHITE = 555;
  public static final int NONE = -1;
  public static final int BLACK = 0;
  /**
   * Each channel can habe a value 0 - 6, 255 is transparent.
   */
  private int[] rgbColorPaletteIndex = new int[6 * 6 * 6];

  public RgbColorPalette() {
    buildRgbColorPaletteIndex();
  }

  public static int mergeRgbColors(int colour1, int colour2, int colour3, int colour4) {
    return (encodeRBGACode(colour4) << 24)
            + (encodeRBGACode(colour3) << 16)
            + (encodeRBGACode(colour2) << 8)
            + encodeRBGACode(colour1);
  }

  public int rgbValueForIndex(int colorCode) {
    return rgbColorPaletteIndex[colorCode];
  }

  /**
   * Encode 4 different color information into a single 32 bit int. Each color is a value in the form of RGB where
   * each value can be between 0 and 5, where 0 is black and 5 is the brightest color of that channel.
   *
   * @param colour1
   * @param colour2
   * @param colour3
   * @param colour4
   * @return
   */
  public static int mergeColorCodes(int colour1, int colour2, int colour3, int colour4) {
    return (encodeColorCode(colour4) << 24)
            + (encodeColorCode(colour3) << 16)
            + (encodeColorCode(colour2) << 8)
            + encodeColorCode(colour1);
  }

  /**
   * Encode the given color code into a 8bit color index.
   *
   * @param colour
   * @return
   */
  public static int encodeColorCode(int colour) {
    if (colour < 0) {
      return 255;
    }
    int r = colour / 100 % 10;
    int g = colour / 10 % 10;
    int b = colour % 10;
    return r * 36 + g * 6 + b;
  }

  /**
   * Encode the given color code into a 8bit color index.
   *
   * @param colour
   * @return
   */
  public static int encodeRBGACode(int colour) {
    if (colour < 0) {
      return 255;
    }

    int r = ((colour >> 16) & 0xff) / (255 / 5);
    int g = ((colour >> 8) & 0xff) / (255 / 5);
    int b = ((colour) & 0xff) / (255 / 5);
    return r * 36 + g * 6 + b;
  }

  private void buildRgbColorPaletteIndex() {
    int index = 0;
    for (int r = 0; r < 6; r++) {
      for (int g = 0; g < 6; g++) {
        for (int b = 0; b < 6; b++) {
          int rr = (r * 255 / 5);
          int gg = (g * 255 / 5);
          int bb = (b * 255 / 5);

          rgbColorPaletteIndex[index++] = rr << 16 | gg << 8 | bb;
        }
      }
    }
  }


}
