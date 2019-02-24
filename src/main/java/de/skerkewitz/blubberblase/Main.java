package de.skerkewitz.blubberblase;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.skerkewitz.enora2d.core.game.GameConfig;
import org.apache.commons.cli.ParseException;

public class Main {

  public static void main(String[] args) throws ParseException {

    // most SNES games used 256x224 pixels since higher resolutions caused slowdow
    GameConfig config = new GameConfig(256, 240, 4, "Blubber Blase", args);

    LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
    lwjglApplicationConfiguration.title = config.name;
    lwjglApplicationConfiguration.width = config.width * 4;
    lwjglApplicationConfiguration.height = config.height * 4;
    new LwjglApplication(new GameListener(config), lwjglApplicationConfiguration);
  }
}