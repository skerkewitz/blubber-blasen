package de.skerkewitz.blubberblase.screen;

import com.badlogic.gdx.graphics.Camera;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.Screen;

import java.io.IOException;

public class ScreenController {

  public final static Screen emptyScreen = new Screen() {

    @Override
    public ScreenAction update(int tickTime) {
      return null;
    }

    @Override
    public void render(int tickTime, Camera camera) throws IOException {

    }

    @Override
    public void screenWillDisappear() {

    }

    @Override
    public void screenWillAppear() {

    }

    @Override
    public void screenDidDisappear() {

    }

    @Override
    public void screenDidAppear() {

    }
  };

  private GameConfig config;
  private Screen currentScreen = emptyScreen;

  public ScreenController(GameConfig config) {
    this.config = config;
  }

  public Screen getCurrentScreen() {
    return currentScreen;
  }

  public boolean handleScreenChange(ScreenAction update, int frameCount) {
    switch (update) {
      case None:
        break;
      case GoTile:
        changeScreen(new TitleScreen(config));
        return true;
      case GoLevel:
        changeScreen(new LevelScreen(config, frameCount, 1));
        return true;
      case GoGameOver:
        break;
    }

    return false;
  }

  public void changeScreen(Screen screen) {
    currentScreen.screenWillDisappear();
    screen.screenWillAppear();
    currentScreen.screenDidDisappear();
    screen.screenDidAppear();

    currentScreen = screen;
  }
}
