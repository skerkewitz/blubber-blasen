package de.skerkewitz.blubberblase.screen;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.Screen;
import de.skerkewitz.enora2d.core.game.world.World;

public abstract class AbstractWorldRenderScreen implements Screen {

  private final GameConfig config;
  private final GameContext gameContext;
  private World world;

  protected AbstractWorldRenderScreen(GameConfig config, GameContext gameContext) {
    this.config = config;
    this.gameContext = gameContext;
  }


  @Override
  public void screenWillDisappear() {
    /* Does nothing, overwrite if needed. */
  }

  @Override
  public void screenWillAppear() {
    /* Does nothing, overwrite if needed. */
  }

  @Override
  public void screenDidDisappear() {
    /* Does nothing, overwrite if needed. */
  }

  @Override
  public void screenDidAppear() {
    /* Does nothing, overwrite if needed. */
  }

  public GameConfig getConfig() {
    return config;
  }

  public GameContext getGameContext() {
    return gameContext;
  }

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }
}
