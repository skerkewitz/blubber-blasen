package de.skerkewitz.blubberblase;

import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.esc.systems.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.systems.RenderSpriteSystem;
import de.skerkewitz.enora2d.backend.awt.game.AwtGame;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.entity.Player;

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

    renderSpriteSystem = new RenderSpriteSystem(screen);
    renderDebugSystem = new RenderDebugSystem(screen);

    world = new MainWorld();

    player = (Player) EntityFactory.spawnBubblun(input);
    world.addEntity(player);
    world.addEntity(EntityFactory.spawnBubble(0, new Point2i(8 * 8, 24 * 8), MoveableLegacyEntity.MoveDirection.Right));
    world.addEntity(EntityFactory.spawnZenChan());
  }

  @Override
  public void render() {

    /* Render the backgroundLayer into the screen. */
    renderLevel(world.backgroundLayer, 0, 0);

    /* Render all the entities. */

    renderSpriteSystem.update(getTickTime(), world, world.getEntityContainer().stream());

    if (gameConfig.cmd.hasOption("showbbox")) {
      renderDebugSystem.update(getTickTime(), world, world.getEntityContainer().stream());
    }

    super.render();
  }
}
