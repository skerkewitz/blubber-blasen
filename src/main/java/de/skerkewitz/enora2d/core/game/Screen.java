package de.skerkewitz.enora2d.core.game;

import java.io.IOException;

public interface Screen {
  void render(int tickTime) throws IOException;
}
