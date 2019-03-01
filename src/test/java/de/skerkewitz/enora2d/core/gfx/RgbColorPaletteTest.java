package de.skerkewitz.enora2d.core.gfx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RgbColorPaletteTest {


  @Test
  void encodeRBGACode() {

    {
      int code1 = RgbColorPalette.encodeRBGACode(0xFF0000);
      int code2 = RgbColorPalette.encodeColorCode(500);
      assertEquals(code1, code2);
    }

    {
      int code1 = RgbColorPalette.encodeRBGACode(0x00FF00);
      int code2 = RgbColorPalette.encodeColorCode(50);
      assertEquals(code1, code2);
    }

    {
      int code1 = RgbColorPalette.encodeRBGACode(0x0000FF);
      int code2 = RgbColorPalette.encodeColorCode(005);
      assertEquals(code1, code2);
    }

    {
      int code1 = RgbColorPalette.encodeRBGACode(0xffffff);
      int code2 = RgbColorPalette.encodeColorCode(555);
      assertEquals(code1, code2);
    }


  }
}