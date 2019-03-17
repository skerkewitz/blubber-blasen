package de.skerkewitz.enora2d.core.ecs;

import com.badlogic.gdx.graphics.Camera;

public interface RenderSystem {
  void applyActiveCamera(Camera camera);
}
