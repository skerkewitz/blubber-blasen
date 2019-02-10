package de.skerkewitz.enora2d.core.gfx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageDataContainer {

  private static final Logger logger = LogManager.getLogger(ImageDataContainer.class);

  private final Map<NamedResource, ImageData> knownResources = new HashMap<>();

  public ImageData getResourceForName(NamedResource resource) throws IOException {

    ImageData imageData = knownResources.get(resource);
    if (imageData == null) {

      logger.info("Cache miss for: " + resource);

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
}
