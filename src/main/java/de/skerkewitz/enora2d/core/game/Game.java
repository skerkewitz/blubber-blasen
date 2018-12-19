package de.skerkewitz.enora2d.core.game;

import java.awt.*;
import java.io.IOException;

public interface Game {
  int[] getFrameBufferPixel();

  void init() throws IOException;

  void start();

  void stop();

  void run();

  void tick();

  void render();

  int getTickTime();

  class GameConfig {

    public final String name;
    public final int width;
    public final int height;

    public final int scale;

    public final Dimension displayDimensions;

    public GameConfig(int width, int height, int scale, String name) {
      this.width = width;
      this.height = height;
      this.scale = scale;
      this.name = name;

      displayDimensions = new Dimension(width * scale, height * scale);
    }
  }
}
