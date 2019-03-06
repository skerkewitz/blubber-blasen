package de.skerkewitz.blubberblase;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.StaticMapContent;
import org.apache.commons.cli.ParseException;

public class Main {

  public static void main(String[] args) throws ParseException {

    // most SNES games used 256x224 pixels since higher resolutions caused slowdow
    GameConfig config = new GameConfig(StaticMapContent.WIDTH, StaticMapContent.HEIGHT, 4, "Blubber Blase", args);

    Lwjgl3ApplicationConfiguration lwjglApplicationConfiguration = new Lwjgl3ApplicationConfiguration();
    lwjglApplicationConfiguration.setWindowedMode(config.width * 4, config.height * 4);
    lwjglApplicationConfiguration.setTitle(config.name);
    new Lwjgl3Application(new GameListener(config), lwjglApplicationConfiguration);
  }
}