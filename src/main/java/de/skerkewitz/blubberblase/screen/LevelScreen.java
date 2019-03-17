package de.skerkewitz.blubberblase.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.blubberblase.esc.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.RenderSpriteSystem;
import de.skerkewitz.blubberblase.esc.RenderTextComponent;
import de.skerkewitz.blubberblase.esc.RenderTextSystem;
import de.skerkewitz.blubberblase.screen.highscore.HighscoreScreen;
import de.skerkewitz.blubberblase.screen.highscore.ScoreText;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.World;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class LevelScreen extends AbstractWorldRenderScreen {

  private SpriteBatch spriteBatch = new SpriteBatch();

  private final Vector3 translateOffset = new Vector3(0, 16, 0);
  private RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem(translateOffset);
  private RenderTextSystem renderTextSystem = new RenderTextSystem(Vector3.Zero);
  private RenderDebugSystem renderDebugSystem = new RenderDebugSystem(translateOffset);

  private ScoreText highscoreText = new ScoreText(0);

  public LevelScreen(GameConfig config, int frameCount, int levelNum) {
    super(config, new GameContext());
    getGameContext().currentLevelNum = levelNum;
    getGameContext().clampLevelNum();

    getGameContext().currentHighScore = HighscoreScreen.loadHighscore().get(0).score;

    setWorld(LevelUtils.loadWorld(frameCount, config, getGameContext().currentLevelNum, null));
    addUiEntites();

    Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (21).wav")).play();
  }

  private void addUiEntites() {
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(11, 1), highscoreText, Color.WHITE));

    /* Update player score. */
    RenderTextComponent component = getWorld().getPlayerScoreEntity().getComponent(RenderTextComponent.class);
    component.text = () -> StringUtils.center("" + getGameContext().scorePlayer1, 10);
  }

  @Override
  public ScreenAction update(int tickTime) {

    GameContext gameContext = getGameContext();
    if (!gameContext.isGameOver()) {
      getWorld().tick(tickTime, gameContext);
    } else {
      if (gameContext.getGameOverTickTime() + TimeUtil.secondsToTickTime(4) < tickTime) {
        return ScreenAction.GoGameOver;
      }
    }
    /* Update player score. */
    highscoreText.score = Math.max(gameContext.currentHighScore, gameContext.scorePlayer1);

    if (tickTime % 10 != 0) {
      return ScreenAction.None;
    }

    GameConfig config = getConfig();
    if (config.noNextLevel) {
      return ScreenAction.None;
    }

    /* Check any 100 ticks if the level is cleared. */
    if (gameContext.isLevelClearedTimer < 0) {
      if (LevelUtils.isLevelCleared(getWorld())) {
        gameContext.isLevelClearedTimer = tickTime + TimeUtil.secondsToTickTime(5);
        Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (16).wav")).play();
      }
    } else {

      if (tickTime > gameContext.isLevelClearedTimer) {
        setWorld(LevelUtils.loadNextLevel(tickTime, gameContext, config, getWorld()));
        addUiEntites();
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
    GameContext gameContext = getGameContext();
    World world = getWorld();
    renderSpriteSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);

    /* Render all the entities. */
    renderTextSystem.applyActiveCamera(camera);
    renderTextSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);

    if (getConfig().cmd.hasOption("showbbox")) {
      renderDebugSystem.applyActiveCamera(camera);
      renderDebugSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);
    }
  }

  private void renderStaticWorld(int tickTime, Camera camera) throws IOException {
    World world = getWorld();
    spriteBatch.setProjectionMatrix(camera.combined.cpy().translate(translateOffset));
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
}
