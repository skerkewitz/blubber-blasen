package de.skerkewitz.enora2d.core.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageData {

    private String path;
    public int width;
    public int height;

    public int[] pixels;

    public ImageData(String path) throws IOException {
        BufferedImage image = ImageIO.read(ImageData.class.getResourceAsStream(path));

        this.path = path;
        this.width = image.getWidth();
        this.height = image.getHeight();

        pixels = image.getRGB(0, 0, width, height, null, 0, width);


        for (int i = 0; i < pixels.length; i++) {
            /* We only care about for different values so we can flatten them. */
            pixels[i] = (pixels[i] & 0xff) / 64;
        }
    }
}
