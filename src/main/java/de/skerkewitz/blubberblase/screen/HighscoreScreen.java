package de.skerkewitz.blubberblase.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.HighscoreScreenWorld;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.blubberblase.esc.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.RenderSpriteSystem;
import de.skerkewitz.blubberblase.esc.RenderTextSystem;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.World;

import java.io.IOException;

public class HighscoreScreen extends AbstractWorldRenderScreen {

  private final Vector3 translateOffset = new Vector3(0, 16, 0);
  private RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem(translateOffset);
  private RenderTextSystem renderTextSystem = new RenderTextSystem(Vector3.Zero);
  private RenderDebugSystem renderDebugSystem = new RenderDebugSystem(translateOffset);

  private SpriteBatch spriteBatch = new SpriteBatch();

  public HighscoreScreen(GameConfig config) {
    super(config, new GameContext());
    setWorld(new HighscoreScreenWorld(config, 0));

    var currentScore = 112560;
    var bestScore = 112560;


    /* Create player score entity. */
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 0), "1UP", Color.GREEN));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(0, 1), " " + currentScore, Color.WHITE));

    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(11, 0), "HIGHSCORE", Color.RED));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(11, 1), "  " + bestScore, Color.WHITE));


    /* Enter initials. */
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(4, 4), "Enter your name player 1", Color.CYAN));

    /* Score and round. */
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(8, 8), " Score  RD        Name", Color.YELLOW));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(8, 10), "112560  10  .F.Tropper", Color.WHITE));

    /* Top five. */
    /* Score and round. */
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 14), "      Score  RD        Name", Color.WHITE));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 16), "1ST  112560  10  ..........", Color.YELLOW));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 18), "2ND   30000  32  .....EmKay", Color.WHITE));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 20), "3RD   30000  28  ...GOD.SON", Color.WHITE));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 22), "4TH   30000  24  ....TUFFYR", Color.WHITE));
    getWorld().addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 24), "5TH   30000  20  .......KIM", Color.WHITE));

    Music music = Gdx.audio.newMusic(Gdx.files.internal("music/highscore.mp3"));
    music.setVolume(0.5f);
    music.play();
  }


  @Override
  public ScreenAction update(int tickTime) {

//    if (!gameContext.gameOver) {
    getWorld().tick(tickTime, getGameContext());
//    }
//
//    if (tickTime % 10 != 0) {
//      return ScreenAction.None;
//    }
//
//    if (config.noNextLevel) {
//      return ScreenAction.None;
//    }
//
//    /* Check any 100 ticks if the level is cleared. */
//    if (gameContext.isLevelClearedTimer < 0) {
//      if (LevelUtils.isLevelCleared(world)) {
//        gameContext.isLevelClearedTimer = tickTime + TimeUtil.secondsToTickTime(5);
//        Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (16).wav")).play();
//      }
//    } else {
//
//      if (tickTime > gameContext.isLevelClearedTimer) {
//        this.world = LevelUtils.loadNextLevel(tickTime, gameContext, config, world);
//        gameContext.isLevelClearedTimer = -1;
//      }
//    }

    return ScreenAction.None;
  }


  @Override
  public void render(int tickTime, Camera camera) throws IOException {

    //renderStaticWorld(tickTime, camera);

    final World world = getWorld();
    final GameContext gameContext = getGameContext();

    /* Render all the entities. */
    renderSpriteSystem.applyActiveCamera(camera);
    renderSpriteSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);

    /* Render all the entities. */
    renderTextSystem.applyActiveCamera(camera);
    renderTextSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);

    if (getConfig().cmd.hasOption("showbbox")) {
      renderDebugSystem.applyActiveCamera(camera);
      renderDebugSystem.update(tickTime, world, world.getEntityContainer().stream(), gameContext);
    }
  }

//  private void renderStaticWorld(int tickTime, Camera camera) throws IOException {
//
//    spriteBatch.setProjectionMatrix(camera.combined.cpy().translate(translateOffset));
//    spriteBatch.begin();
//    for (int y = 0; y < world.numVerticalTiles; y++) {
//      for (int x = 0; x < world.numHorizontalTiles; x++) {
//
//        final Sprite sprite = world.staticMapContent.getTileSprite(x, y);
//        if (sprite == null) {
//          continue;
//        }
//
//        sprite.setPosition(x * 8, y * 8);
//        sprite.draw(spriteBatch);
//      }
//    }
//    spriteBatch.end();
//  }

}
