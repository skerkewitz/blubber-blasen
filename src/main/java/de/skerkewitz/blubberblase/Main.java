package de.skerkewitz.blubberblase;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.esc.systems.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.systems.RenderSpriteSystem;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.game.Game;
import de.skerkewitz.enora2d.core.input.GdxInputHandler;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);


  public static void main(String[] args) throws ParseException {

    // most SNES games used 256x224 pixels since higher resolutions caused slowdow
    Game.GameConfig config = new Game.GameConfig(256, 224, 4, "Blubber Blase", args);

    LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
    lwjglApplicationConfiguration.title = config.name;
    lwjglApplicationConfiguration.width = config.width;
    lwjglApplicationConfiguration.height = config.height;
    new LwjglApplication(new GameListener(config), lwjglApplicationConfiguration);
//
//    Game game = new MainGame(config);
//    game.start();
  }

  private static class GameListener implements ApplicationListener {

    private final Game.GameConfig config;
    private RenderDebugSystem renderDebugSystem;
    private RenderSpriteSystem renderSpriteSystem;
    private MainWorld world;

    private int tickTime = 0;

    private Viewport viewport;
    private Camera camera;

    public GameListener(Game.GameConfig config) {
      this.config = config;
    }

    @Override
    public void create() {

      renderSpriteSystem = new RenderSpriteSystem(null);
      renderDebugSystem = new RenderDebugSystem(null);

      camera = new OrthographicCamera(config.width, config.height);
      ((OrthographicCamera) camera).setToOrtho(true);
      viewport = new FitViewport(config.width, config.height, camera);


      camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
      camera.update();

      world = new MainWorld();

      world.addEntity(EntityFactory.spawnBubblun(new GdxInputHandler()));
      world.addEntity(EntityFactory.spawnBubble(0, new Point2i(8 * 8, 24 * 8), MoveableLegacyEntity.MoveDirection.Right));
      world.addEntity(EntityFactory.spawnZenChan());
    }

    @Override
    public void resize(int width, int height) {
      viewport.update(width, height, true);
    }

    @Override
    public void render() {
      camera.update();


      tickTime++;
      world.tick(tickTime, camera.combined);

      Gdx.gl.glClearColor(0.0f, 0, 0.0f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      /* Render the backgroundLayer into the screen. */
//      renderLevel(world.backgroundLayer, 0, 0);

      /* Render all the entities. */

      renderSpriteSystem.update(tickTime, world, world.getEntityContainer().stream());

//      if (gameConfig.cmd.hasOption("showbbox")) {
//        renderDebugSystem.update(tickTime, world, world.getEntityContainer().stream());
//      }
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
}