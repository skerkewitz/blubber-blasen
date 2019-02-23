package de.skerkewitz.blubberblase;

import de.skerkewitz.blubberblase.esc.systems.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.systems.RenderSpriteSystem;
import de.skerkewitz.enora2d.backend.awt.game.AwtGame;

import java.io.IOException;

public class MainGame extends AwtGame {

  private RenderSpriteSystem renderSpriteSystem;
  private RenderDebugSystem renderDebugSystem;

  public MainGame(GameConfig config) {
    super(config);
  }

  @Override
  public void init() throws IOException {
    super.init();


  }

  @Override
  public void render() {



    super.render();
  }
}
