package de.skerkewitz.enora2d.core.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GdxTextureContainer {

  private final Map<NamedResource, List<Entry>> knownResources = new HashMap<>();

  private final RgbColorPalette rgbColorPalette = new RgbColorPalette();

  public Sprite getTextureNamedResourceAndPalette(NamedResource resource, int palette, ImageDataContainer imageDataContainer) {

    var entries = knownResources.get(resource);
    if (entries != null) {
      for (var entry : entries) {
        if (entry.palette == palette) {
          return entry.sprite;
        }
      }
    }

    Gdx.app.log(this.getClass().getSimpleName(), "Cache miss for: " + resource + " palette " + palette);
    final ImageData imageData = imageDataContainer.getResourceForName(resource);

    final Pixmap pixmap = new Pixmap(imageData.width, imageData.width, Pixmap.Format.RGBA8888);

    /* Render the screen into the framebuffer. */
    for (int y = 0; y < pixmap.getHeight(); y++) {
      for (int x = 0; x < pixmap.getWidth(); x++) {
        int colourCode = (palette >> (imageData.pixels[x + y * imageData.width] * 8)) & 255;
        if (colourCode < 255) {
          pixmap.drawPixel(x, y, (rgbColorPalette.rgbValueForIndex(colourCode) << 8) | 255); // color
        } else {
          pixmap.drawPixel(x, y, 0x00000000); // transparent
        }
      }
    }

    var sprite = new Sprite(new Texture(pixmap, false));

    Entry entry = new Entry(sprite, palette);

    if (entries != null) {
      entries.add(entry);
    } else {
      var list = new ArrayList<Entry>();
      list.add(entry);
      knownResources.put(resource, list);
    }

    return sprite;
  }

  private static class Entry {
    public final Sprite sprite;
    public final int palette;

    public Entry(Sprite sprite, int palette) {
      this.sprite = sprite;
      this.palette = palette;
    }
  }
}
