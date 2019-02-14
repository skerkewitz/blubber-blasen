package de.skerkewitz.blubberblase;

import de.skerkewitz.enora2d.core.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);


  public static void main(String[] args) {

    // most SNES games used 256x224 pixels since higher resolutions caused slowdow
    Game.GameConfig config = new Game.GameConfig(256, 224, 4, "Blubber Blase");

    Game game = new MainGame(config);
    game.start();
  }
}