package de.skerkewitz.blubberblase;

import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.esc.systems.RenderSpriteSystem;
import de.skerkewitz.enora2d.backend.awt.game.AwtGame;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.entity.Player;

import java.io.IOException;

public class MainGame extends AwtGame {

  private RenderSpriteSystem renderSpriteSystem;

  public MainGame(GameConfig config) {
    super(config);
  }

  @Override
  public void init() throws IOException {
    super.init();

    renderSpriteSystem = new RenderSpriteSystem(screen);

    level = new MainLevel();

    player = (Player) EntityFactory.spawnBubblun(input);
    level.addEntity(player);
    level.addEntity(EntityFactory.spawnBubble(0, new Point2i(8 * 8, 24 * 8)));
    level.addEntity(EntityFactory.spawnZenChan());
  }

  @Override
  public void render() {

    /* Render the backgroundLayer into the screen. */
    renderLevel(level.backgroundLayer, 0, 0);

    /* Render all the entities. */

    renderSpriteSystem.update(getTickTime(), level, level.getEntityContainer().stream());

    super.render();
  }
}
