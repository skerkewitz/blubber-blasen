package de.skerkewitz.blubberblase;

import de.skerkewitz.enora2d.backend.awt.game.AwtGame;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import de.skerkewitz.enora2d.core.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) {

    logger.info("Hello world");

    // most SNES games used 256x224 pixels since higher resolutions caused slowdow
    Game.GameConfig config = new Game.GameConfig(256, 224, 5, "Blubber Blase");

    AbstractGame game = new AwtGame(config);
    ((AwtGame) game).initBackend();

    game.debug = true;

    game.start();
  }
}