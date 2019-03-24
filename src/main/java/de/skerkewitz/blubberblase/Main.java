package de.skerkewitz.blubberblase;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.StaticMapContent;
import org.apache.commons.cli.ParseException;

public class Main {

  public static void main(String[] args) throws ParseException {

    // most SNES games used 256x224 pixels since higher resolutions caused slowdow
    final int width = StaticMapContent.WIDTH;
    final int screenHeight = StaticMapContent.HEIGHT + 16; // some extra space for the score.
    final GameConfig config = new GameConfig(width, screenHeight, 4, "Blubber Blasen", args);

    final Lwjgl3ApplicationConfiguration lwjglApplicationConfiguration = new Lwjgl3ApplicationConfiguration();
    lwjglApplicationConfiguration.setTitle(config.name);

//    lwjglApplicationConfiguration.disableAudio(true);

    if (config.fullscreen) {
      lwjglApplicationConfiguration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
    } else {
      lwjglApplicationConfiguration.setWindowedMode(config.width * 4, config.height * 4);
    }

    new Lwjgl3Application(new GameListener(config), lwjglApplicationConfiguration);
  }
}