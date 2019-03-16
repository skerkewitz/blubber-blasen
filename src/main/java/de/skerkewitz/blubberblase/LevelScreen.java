package de.skerkewitz.blubberblase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.blubberblase.esc.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.RenderSpriteSystem;
import de.skerkewitz.blubberblase.esc.RenderTextSystem;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.Screen;
import de.skerkewitz.enora2d.core.game.world.World;

import java.io.IOException;

public class LevelScreen implements Screen {

  private final GameConfig config;
  private final GameContext gameContext;
  private SpriteBatch spriteBatch = new SpriteBatch();
  private World world;

  private RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem();
  private RenderTextSystem renderTextSystem = new RenderTextSystem();
  private RenderDebugSystem renderDebugSystem = new RenderDebugSystem();

  public LevelScreen(GameConfig config, int frameCount, int levelNum) {
    this.gameContext = new GameContext();
    this.gameContext.currentLevelNum = levelNum;
    this.gameContext.clampLevelNum();

    this.config = config;
    this.world = LevelUtils.loadWorld(frameCount, config, gameContext.currentLevelNum, null);

    Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (21).wav")).play();
  }




  @Override
  public ScreenAction update(int tickTime) {

    if (!gameContext.gameOver) {
      world.tick(tickTime, gameContext);
    }

    if (tickTime % 10 != 0) {
      return ScreenAction.None;
    }

    if (config.noNextLevel) {
      return ScreenAction.None;
    }

    /* Check any 100 ticks if the level is cleared. */
    if (gameContext.isLevelClearedTimer < 0) {
      if (LevelUtils.isLevelCleared(world)) {
        gameContext.isLevelClearedTimer = tickTime + TimeUtil.secondsToTickTime(5);
        Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (16).wav")).play();
      }
    } else {

      if (tickTime > gameContext.isLevelClearedTimer) {
        this.world = LevelUtils.loadNextLevel(tickTime, gameContext, config, world);
        gameContext.isLevelClearedTimer = -1;
      }
    }

    return ScreenAction.None;
  }


  @Override
  public void render(int tickTime, Camera camera) throws IOException {

    renderStaticWorld(tickTime, camera);

    /* Render all the entities. */
    renderSpriteSystem.applyActiveCamera(camera);
    renderSpriteSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);

    /* Render all the entities. */
    renderTextSystem.applyActiveCamera(camera);
    renderTextSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);

    if (config.cmd.hasOption("showbbox")) {
      renderDebugSystem.applyActiveCamera(camera);
      renderDebugSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);
    }
  }

  private void renderStaticWorld(int tickTime, Camera camera) throws IOException {

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();
    for (int y = 0; y < world.numVerticalTiles; y++) {
      for (int x = 0; x < world.numHorizontalTiles; x++) {

        final Sprite sprite = world.staticMapContent.getTileSprite(x, y);
        if (sprite == null) {
          continue;
        }

        sprite.setPosition(x * 8, y * 8);
        sprite.draw(spriteBatch);
      }
    }
    spriteBatch.end();
  }

  @Override
  public void screenWillDisappear() {

  }

  @Override
  public void screenWillAppear() {

  }

  @Override
  public void screenDidDisappear() {

  }

  @Override
  public void screenDidAppear() {

  }
}
