package de.skerkewitz.enora2d.core.game;

import com.badlogic.gdx.graphics.Camera;

import java.io.IOException;

public interface Screen {

  void update(int tickTime);

  void render(int tickTime, Camera camera) throws IOException;
}
