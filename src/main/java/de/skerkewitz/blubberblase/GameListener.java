package de.skerkewitz.blubberblase;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bitfire.utils.ShaderLoader;
import de.skerkewitz.blubberblase.screen.LevelScreen;
import de.skerkewitz.blubberblase.screen.ScreenAction;
import de.skerkewitz.blubberblase.screen.ScreenController;
import de.skerkewitz.blubberblase.screen.TitleScreen;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.Screen;

import java.io.IOException;

import static de.skerkewitz.enora2d.core.game.GameConfig.CMD_OPTION_DISABLEPPFX;

class GameListener implements ApplicationListener {

  private final GameConfig config;

  /**
   * The actual frame count we are currently on.
   */
  private int frameCount = 0;

  private Viewport viewport;
  private Camera camera;
  private final ScreenController screenController;
  private PostProcessing post;
  private long startMs;

  public GameListener(GameConfig config) {
    this.config = config;
    screenController = new ScreenController(config);
  }

  @Override
  public void create() {

    camera = new OrthographicCamera(config.width, config.height);
    ((OrthographicCamera) camera).setToOrtho(true);
    viewport = new FitViewport(config.width, config.height, camera);

    camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
    camera.update();

    ShaderLoader.BasePath = "shaders/";
//    plex = new InputMultiplexer();
//    plex.addProcessor( this );
//    Gdx.input.setInputProcessor( plex );
    post = new PostProcessing();
    startMs = TimeUtils.millis();

    //currentScreen = new LevelScreen(config);
    if (config.cmd.hasOption(GameConfig.CMD_OPTION_LEVEL)) {
      int level = Integer.parseInt(config.cmd.getOptionValue(GameConfig.CMD_OPTION_LEVEL));
      screenController.changeScreen(new LevelScreen(config, frameCount, level));
    } else {
      screenController.changeScreen(new TitleScreen(config));
    }
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
  }

  @Override
  public void render() {
    frameCount++;

    final Screen currentScreen = screenController.getCurrentScreen();
    final ScreenAction update = currentScreen.update(frameCount);
    boolean didScreenChange = screenController.handleScreenChange(update, frameCount, currentScreen.getGameContext());
    if (didScreenChange) {
      return;
    }

    post.setEnabled(!config.cmd.hasOption(CMD_OPTION_DISABLEPPFX));

    float elapsedSecs = (float) (TimeUtils.millis() - startMs) / 1000;

    // post-processing
    post.update(elapsedSecs);

    post.enableBlending();
    Gdx.gl.glClearColor(0.0f, 0, 0.0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    post.begin();

    camera.update();

    try {
      /* Render the staticMapContent into the screen. */
      currentScreen.render(frameCount, camera);
    } catch (IOException e) {
      e.printStackTrace();
    }

    post.end();

  }


  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    post.dispose();

  }
}
