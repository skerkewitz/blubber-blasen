package de.skerkewitz.blubberblase;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.skerkewitz.enora2d.core.game.Game;
import de.skerkewitz.enora2d.core.game.Screen;

import java.io.IOException;

class GameListener implements ApplicationListener {

  private final Game.GameConfig config;

  private int tickTime = 0;

  private Viewport viewport;
  private Camera camera;
  private Screen currentScreen;

  public GameListener(Game.GameConfig config) {
    this.config = config;
  }

  @Override
  public void create() {

    camera = new OrthographicCamera(config.width, config.height);
    ((OrthographicCamera) camera).setToOrtho(true);
    viewport = new FitViewport(config.width, config.height, camera);

    camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
    camera.update();

    currentScreen = new LevelScreen(config, null);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
  }

  @Override
  public void render() {
    tickTime++;
    currentScreen.update(tickTime);

    camera.update();
    Gdx.gl.glClearColor(0.0f, 0, 0.0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    try {
      /* Render the backgroundLayer into the screen. */
      currentScreen.render(tickTime, camera);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }
}
