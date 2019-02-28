package de.skerkewitz.enora2d.core.game;

import com.badlogic.gdx.graphics.Camera;
import de.skerkewitz.blubberblase.ScreenAction;

import java.io.IOException;

public interface Screen {


  ScreenAction update(int tickTime);

  void render(int tickTime, Camera camera) throws IOException;

  void screenWillDisappear();

  void screenWillAppear();

  void screenDidDisappear();

  void screenDidAppear();
}
