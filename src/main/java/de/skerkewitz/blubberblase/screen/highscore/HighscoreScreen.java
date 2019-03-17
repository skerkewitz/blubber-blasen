package de.skerkewitz.blubberblase.screen.highscore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.HighscoreScreenWorld;
import de.skerkewitz.blubberblase.esc.RenderDebugSystem;
import de.skerkewitz.blubberblase.esc.RenderSpriteSystem;
import de.skerkewitz.blubberblase.esc.RenderTextSystem;
import de.skerkewitz.blubberblase.screen.AbstractWorldRenderScreen;
import de.skerkewitz.blubberblase.screen.ScreenAction;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.World;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.skerkewitz.blubberblase.entity.EntityFactory.spawnTextEntity;
import static de.skerkewitz.blubberblase.entity.LevelUtils.convertTileToWorldSpace;

public class HighscoreScreen extends AbstractWorldRenderScreen implements InputProcessor {

  private enum State {
    EnterName,
    GameOver,
    LeaveScreen
  }

  private final Vector3 translateOffset = new Vector3(0, 16, 0);
  private final List<Entity> enterNamesEntities = new ArrayList<>();
  private State state = State.EnterName;
  private RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem(translateOffset);
  private RenderTextSystem renderTextSystem = new RenderTextSystem(Vector3.Zero);
  private RenderDebugSystem renderDebugSystem = new RenderDebugSystem(translateOffset);

  private ScoreText currentScore = new ScoreText(0);
  private ScoreText bestScore = new ScoreText(0);
  private EnterNameScoreText enterNameScoreText = new EnterNameScoreText(112560, 2);
  private List<HighscoreText> highscore;
  private int playerHighScoreIndex = 0;
  private Music music = Gdx.audio.newMusic(Gdx.files.internal("music/highscore.mp3"));

  public HighscoreScreen(GameConfig config, GameContext gameContext) {
    super(config, gameContext);
    setWorld(new HighscoreScreenWorld(config, 0));

    this.highscore = loadHighscore();

    /* Create player score entity. */
    getWorld().addEntity(spawnTextEntity(convertTileToWorldSpace(3, 0), () -> "1UP", Color.GREEN));
    getWorld().addEntity(spawnTextEntity(convertTileToWorldSpace(0, 1), currentScore, Color.WHITE));

    getWorld().addEntity(spawnTextEntity(convertTileToWorldSpace(11, 0), () -> "HIGHSCORE", Color.RED));
    getWorld().addEntity(spawnTextEntity(convertTileToWorldSpace(11, 1), bestScore, Color.WHITE));


    /* Enter initials. */
    enterNamesEntities.add(spawnTextEntity(convertTileToWorldSpace(4, 4), () -> "Enter your name player 1", Color.CYAN));
    enterNamesEntities.add(spawnTextEntity(convertTileToWorldSpace(8, 8), () -> " Score  RD        Name", Color.YELLOW));
    enterNamesEntities.add(spawnTextEntity(convertTileToWorldSpace(8, 10), enterNameScoreText, Color.WHITE));

    enterNamesEntities.forEach(entity -> {
      getWorld().addEntity(entity);
    });

    setup(gameContext.scorePlayer1, gameContext.currentLevelNum);

    /* Top five. */
    /* Score and round. */
    getWorld().addEntity(spawnTextEntity(convertTileToWorldSpace(3, 14), () -> "      Score  RD        Name", Color.WHITE));
    for (int i = 0; i < Math.min(5, highscore.size()); i++) {
      final Color color = i == playerHighScoreIndex ? Color.YELLOW : Color.WHITE;
      getWorld().addEntity(spawnTextEntity(convertTileToWorldSpace(3, 16 + (i * 2)), highscore.get(i), color));
    }
  }

  private void setup(int playerScore, int round) {

    bestScore.score = Math.max(highscore.get(0).score, playerScore);
    currentScore.score = playerScore;
    enterNameScoreText.score = playerScore;

    /* Find the position where there score in the highscore list is less then our score. */
    playerHighScoreIndex = highscore.size();
    Optional<HighscoreText> first = highscore.stream().filter(highscoreText -> highscoreText.score < playerScore).findFirst();
    if (first.isPresent()) {
      playerHighScoreIndex = highscore.indexOf(first.get());
    } else {
      setStateGameOver();
    }

    highscore.add(playerHighScoreIndex, new HighscoreText(0, playerScore, round, ""));

    for (int i = 0; i < highscore.size(); i++) {
      highscore.get(i).place = i;
    }

  }

  public static List<HighscoreText> loadHighscore() {

    List<HighscoreText> highscore = new ArrayList<>();

    try {
      Preferences prefs = Gdx.app.getPreferences("highscore");
      for (int i = 0; i < 5; i++) {
        String sIdx = StringUtils.leftPad("" + i, 2, '0');
        String[] split = StringUtils.split(prefs.getString("place-" + sIdx), ";");
        highscore.add(new HighscoreText(i, Integer.parseInt(split[0]), Integer.parseInt(split[1]), split[2]));
      }

      return highscore;
    } catch (Exception e) {
      Gdx.app.log(HighscoreScreen.class.getSimpleName(), "Could not load high score because of: " + e, e);
    }

    /* Load the current highscore. */
    highscore.clear();
    highscore.add(new HighscoreText(0, 25000, 10, "Rascal"));
    highscore.add(new HighscoreText(1, 15000, 8, "VonBlubba"));
    highscore.add(new HighscoreText(2, 10000, 4, "Banebou"));
    highscore.add(new HighscoreText(3, 5000, 2, "Pulpul"));
    highscore.add(new HighscoreText(4, 1000, 1, "Zenchan"));

    return highscore;
  }

  public static void storeHighscore(List<HighscoreText> highscore) {

    Preferences prefs = Gdx.app.getPreferences("highscore");
    prefs.clear();

    for (int i = 0; i < 5; i++) {
      String sIdx = StringUtils.leftPad("" + i, 2, '0');
      prefs.putString("place-" + sIdx, "" + highscore.get(i).score + ";" + highscore.get(i).round + ";" + highscore.get(i).name);
    }
  }

  private void confirmName() {
    highscore.get(playerHighScoreIndex).name = this.enterNameScoreText.name;
    storeHighscore(highscore);
    setStateGameOver();
  }

  private void setStateGameOver() {
    enterNamesEntities.forEach(entity -> entity.expired());
    getWorld().addEntity(spawnTextEntity(convertTileToWorldSpace(7, 8), () -> "G A M E    O V E R", Color.RED));
    state = State.GameOver;
  }


  @Override
  public ScreenAction update(int tickTime) {

//    if (!gameContext.gameOver) {
    getWorld().tick(tickTime, getGameContext());


    if (state == State.LeaveScreen) {
      return ScreenAction.GoTile;
    }

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

  @Override
  public void screenWillDisappear() {
    super.screenWillDisappear();
    music.stop();
    Gdx.input.setInputProcessor(null);
  }

  @Override
  public void screenDidAppear() {
    super.screenDidAppear();

    Gdx.input.setInputProcessor(this);

    music.setVolume(0.5f);
    music.play();
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
//    if (keycode == Input.Keys.BACKSPACE) {
//      this.enterNameScoreText.name = this.enterNameScoreText.name + "a";
//      return true;
//    }
    return false;
  }

  @Override
  public boolean keyTyped(char character) {

    if (state == State.GameOver) {
      state = State.LeaveScreen;
      return true;
    }

    if (character == '\b') {
      this.enterNameScoreText.name = StringUtils.chop(this.enterNameScoreText.name);
    } else if (character == '\n') {
      confirmName();
    } else {
      this.enterNameScoreText.name = this.enterNameScoreText.name + character;
      this.enterNameScoreText.name = StringUtils.truncate(this.enterNameScoreText.name, 10);
    }

    return true;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
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
