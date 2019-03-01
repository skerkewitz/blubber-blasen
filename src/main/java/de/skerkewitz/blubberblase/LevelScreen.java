package de.skerkewitz.blubberblase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.blubberblase.esc.component.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.component.RenderSpriteSystem;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.TimeUtil;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.Screen;
import de.skerkewitz.enora2d.core.game.world.StaticMapContentLoader;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.game.world.tiles.BasicTile;
import de.skerkewitz.enora2d.core.gfx.GdxTextureContainer;
import de.skerkewitz.enora2d.core.gfx.ImageDataContainer;
import de.skerkewitz.enora2d.core.input.GdxKeyboardInputHandler;
import de.skerkewitz.enora2d.core.input.InputHandler;

import java.io.IOException;

public class LevelScreen implements Screen {

  private final GameConfig config;
  private final GameContext gameContext;
  private SpriteBatch spriteBatch = new SpriteBatch();
  private World world;
  private ImageDataContainer imageDataContainer = new ImageDataContainer();
  private GdxTextureContainer gdxTextureContainer = new GdxTextureContainer();

  private RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem();
  private RenderDebugSystem renderDebugSystem = new RenderDebugSystem();

  public LevelScreen(GameConfig config, int frameCount) {
    this.gameContext = new GameContext();

    this.config = config;
    this.world = loadWorldOfLevel(frameCount, config, gameContext.currentLevelNum);

    Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (21).wav")).play();
  }

  public static World loadWorldOfLevel(int frameCount, GameConfig config, int level) {
    var world = new MainWorld(config, StaticMapContentLoader.load(level), frameCount);

//    Controller first = Controllers.getControllers().first();
//    InputHandler handler = first == null ? new GdxKeyboardInputHandler() : new GdxGamepadInputHandler(first);
    InputHandler handler = new GdxKeyboardInputHandler();

    world.addPlayer(EntityFactory.spawnBubblun(handler));

    switch (level) {
      case 1:
        world.prepareSpawnAtTime(frameCount + 1, EntityFactory.spawnZenChan(new Point2f(15 * 8, 4 * 8), frameCount));
        world.prepareSpawnAtTime(frameCount + 21, EntityFactory.spawnZenChan(new Point2f(15 * 8, 4 * 8), frameCount));
        world.prepareSpawnAtTime(frameCount + 41, EntityFactory.spawnZenChan(new Point2f(15 * 8, 4 * 8), frameCount));
        break;
      case 2:
        world.prepareSpawnAtTime(frameCount + 1, EntityFactory.spawnZenChan(new Point2f(14 * 8, 4 * 8), frameCount));
        world.prepareSpawnAtTime(frameCount + 1, EntityFactory.spawnZenChan(new Point2f(16 * 8, 4 * 8), frameCount));
        world.prepareSpawnAtTime(frameCount + 31, EntityFactory.spawnZenChan(new Point2f(12 * 8, 4 * 8), frameCount));
        world.prepareSpawnAtTime(frameCount + 31, EntityFactory.spawnZenChan(new Point2f(18 * 8, 4 * 8), frameCount));
        break;
      default:
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
        final BasicTile tile = (BasicTile) world.staticMapContent.getTile(x, y);

        int xTile = tile.getTileId() % 32;
        int yTile = tile.getTileId() / 32;

        Sprite sprite = gdxTextureContainer.getTextureNamedResourceAndPalette(Ressources.SpriteSheet, tile.getTileColour(), imageDataContainer);
        sprite.setSize(8, 8);
        sprite.setPosition(x * 8, y * 8);
        sprite.setRegion(xTile * 8, yTile * 8, 8, 8);
        sprite.setFlip(false, true);
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
