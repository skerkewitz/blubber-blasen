package de.skerkewitz.enora2d.core.gfx;

import com.badlogic.gdx.Gdx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageDataContainer {

  private final Map<NamedResource, ImageData> knownResources = new HashMap<>();

  private ImageData getResourceForNameInternal(NamedResource resource) throws IOException {

    ImageData imageData = knownResources.get(resource);
    if (imageData == null) {
      Gdx.app.log(this.getClass().getSimpleName(), "Cache miss for: " + resource);

      BufferedImage image = ImageIO.read(ImageDataContainer.class.getResourceAsStream(resource.name));

      int width = image.getWidth();
      int height = image.getHeight();
      int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

      for (int i = 0; i < pixels.length; i++) {
        /* We only care about four different values so we can flatten them. */
        pixels[i] = (pixels[i] & 0xff) / 64;
      }

      imageData = new ImageData(width, height, pixels);
      knownResources.put(resource, imageData);
    }

    return imageData;
  }

  public ImageData getResourceForName(NamedResource resource) {
    try {
      return getResourceForNameInternal(resource);
    } catch (IOException e) {
      throw new RuntimeException("Could not load resrouce " + resource.name + " because of " + e, e);
    }
  }
}
