package de.skerkewitz.blubberblase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.gierzahn.editor.map.EnemyBaseMapLayer;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.blubberblase.esc.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.RenderSpriteSystem;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.Screen;
import de.skerkewitz.enora2d.core.game.world.StaticMapContent;
import de.skerkewitz.enora2d.core.game.world.StaticMapContentLoader;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.gfx.GdxTextureContainer;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import de.skerkewitz.enora2d.core.input.GdxKeyboardInputHandler;
import de.skerkewitz.enora2d.core.input.InputHandler;

import java.io.IOException;
import java.util.ArrayList;

public class LevelScreen implements Screen {

  private final GameConfig config;
  private final GameContext gameContext;
  private SpriteBatch spriteBatch = new SpriteBatch();
  private World world;
  private ImageDataContainer imageDataContainer = new ImageDataContainer();
  private GdxTextureContainer gdxTextureContainer = new GdxTextureContainer();

  private RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem();
  private RenderDebugSystem renderDebugSystem = new RenderDebugSystem();

  public LevelScreen(GameConfig config, int frameCount, int levelNum) {
    this.gameContext = new GameContext();
    this.gameContext.currentLevelNum = levelNum;
    this.gameContext.clampLevelNum();

    this.config = config;
    this.world = loadWorldOfLevel(frameCount, config, gameContext.currentLevelNum);

    Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (21).wav")).play();
  }

  public static World loadWorldOfLevel(int frameCount, GameConfig config, int level) {
    StaticMapContent staticMapContent = StaticMapContentLoader.load(level);
    var world = new MainWorld(config, staticMapContent, frameCount);

//    Controller first = Controllers.getControllers().first();
//    InputHandler handler = first == null ? new GdxKeyboardInputHandler() : new GdxGamepadInputHandler(first);
    InputHandler handler = new GdxKeyboardInputHandler();

    world.addPlayer(EntityFactory.spawnBubblun(handler));

    ArrayList<EnemyBaseMapLayer.Enemy> enemySpawnList = staticMapContent.getEnemySpawnList();
    for (EnemyBaseMapLayer.Enemy enemy : enemySpawnList) {
      Point2f position = new Point2f((enemy.x * 8) + 8, (enemy.y * 8) + 12);
      MoveDirection moveDirection = enemy.isLookingLeft ? MoveDirection.Left : MoveDirection.Right;
      world.prepareSpawnAtTime(frameCount, EntityFactory.spawnZenChan(position, frameCount, moveDirection, false));
    }
    return world;
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
        this.world = LevelUtils.loadNextLevel(tickTime, gameContext, config);
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
