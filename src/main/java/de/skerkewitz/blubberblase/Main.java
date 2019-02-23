package de.skerkewitz.blubberblase;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import de.skerkewitz.enora2d.core.game.Game;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);


  public static void main(String[] args) throws ParseException {


    LwjglApplicationConfiguration config_ = new LwjglApplicationConfiguration();
    config_.title = "Drop";
    config_.width = 800;
    config_.height = 480;
    new LwjglApplication(new Drop(), config_);

    // most SNES games used 256x224 pixels since higher resolutions caused slowdow
    Game.GameConfig config = new Game.GameConfig(256, 224, 4, "Blubber Blase", args);

    Game game = new MainGame(config);
    game.start();
  }

  private static class Drop implements ApplicationListener {

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
      Gdx.gl.glClearColor(1.0f, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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