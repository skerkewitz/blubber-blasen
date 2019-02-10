package de.skerkewitz.enora2d.core.game;

import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.enora2d.backend.awt.game.WindowHandler;
import de.skerkewitz.enora2d.core.ecs.system.RenderSpriteSystem;
import de.skerkewitz.enora2d.core.entity.Player;
import de.skerkewitz.enora2d.core.game.level.BackgroundLayer;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.ImageData;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.Screen;
import de.skerkewitz.enora2d.core.gfx.SpriteSheet;
import de.skerkewitz.enora2d.core.input.InputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;


public abstract class AbstractGame extends Canvas implements Runnable, Game {

  private static final Logger logger = LogManager.getLogger(AbstractGame.class);
  public static final double TARGET_FPS = 60D;

  private final RgbColorPalette rgbColorPalette = new RgbColorPalette();

  public final GameConfig gameConfig;
  public boolean running = false;

  private int tickTime = 0;
  public InputHandler input;
  public WindowHandler windowHandler;
  public Player player;

  public boolean debug = true;
  private Thread thread;
  private int[] frameBufferPixels;

  private Screen screen;


  public Level level;
  private SpriteSheet spritesheet;
  private Screen.Sprite sprite;

  public static final int TICKTIME_1s = AbstractGame.secondsToTickTime(1);
  public static final int TICKTIME_5s = AbstractGame.secondsToTickTime(5);
  private boolean paused = false;

  private RenderSpriteSystem renderSpriteSystem;


  public AbstractGame(GameConfig config) {
    super();
    this.gameConfig = config;
  }

  public static int secondsToTickTime(double seconds) {
    return (int) (seconds * AbstractGame.TARGET_FPS);
  }

  @Override
  public void init() throws IOException {

    frameBufferPixels = getFrameBufferPixel();

    screen = new Screen(gameConfig.width, gameConfig.height, new ImageData("/sprite_sheet.png"));

    renderSpriteSystem = new RenderSpriteSystem(screen);
    level = new Level();

    player = (Player) EntityFactory.spawnBubblun(input);
    level.addEntity(player);
    level.addEntity(EntityFactory.spawnBubble(0, 8 * 8, 24 * 8, 1));
    level.addEntity(EntityFactory.spawnZenChan());
  }


  @Override
  public synchronized void start() {
    running = true;

    thread = new Thread(this, gameConfig.name + "_gameLoop");
    thread.start();
  }

  @Override
  public synchronized void stop() {
    running = false;

    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {

    //This value would probably be stored elsewhere.
    final double GAME_HERTZ = 60.0;
    //Calculate how many ns each frame should take for our target game hertz.
    final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
    //At the very most we will update the game this many times before a new render.
    //If you're worried about visual hitches more than perfect timing, set this to 1.
    final int MAX_UPDATES_BEFORE_RENDER = 1;
    //We will need the last update time.
    double lastUpdateTime = System.nanoTime();
    //Store the last time we rendered.
    double lastRenderTime = System.nanoTime();

    //If we are able to get as high as this FPS, don't render again.
    final double TARGET_FPS1 = 60;
    final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS1;

    //Simple way of finding FPS.
    int lastSecondTime = (int) (lastUpdateTime / 1000000000);

    try {
      init();
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize because of: " + e, e);
    }

    int frameCount = 0;

    while (running) {
      double now = System.nanoTime();
      int updateCount = 0;


      if (!paused) {
        //Do as many game updates as we need to, potentially playing catchup.
        while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
          tick();
          lastUpdateTime += TIME_BETWEEN_UPDATES;
          updateCount++;
        }

        //If for some reason an update takes forever, we don't want to do an insane number of catchups.
        //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
        if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
          lastUpdateTime = now - TIME_BETWEEN_UPDATES;
        }

        //Render. To do so, we need to calculate interpolation for a smooth render.
        float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
        render();// drawGame(interpolation);
        frameCount++;
        lastRenderTime = now;

        //Update the frames we got.
        int thisSecond = (int) (lastUpdateTime / 1000000000);
        if (thisSecond > lastSecondTime) {
//          System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
          //fps = frameCount;
          frameCount = 0;
          lastSecondTime = thisSecond;
        }

        //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
        while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
//          Thread.yield();
//
//          //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
//          //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
//          //FYI on some OS's this can cause pretty bad stuttering.
//          try {Thread.sleep(5);} catch(Exception e) {}

          now = System.nanoTime();
        }
      }
    }

  }


  @Override
  public void tick() {
    tickTime++;
    level.tick(tickTime);
  }


  private void renderLevel(BackgroundLayer backgroundLayer, int xOffset, int yOffset) {
    if (xOffset < 0) {
      xOffset = 0;
    }
    if (xOffset > ((backgroundLayer.tileWidth << 3) - screen.screenImageData.width)) {
      xOffset = ((backgroundLayer.tileWidth << 3) - screen.screenImageData.width);
    }
    if (yOffset < 0) {
      yOffset = 0;
    }
    if (yOffset > ((backgroundLayer.tileHeight << 3) - screen.screenImageData.height)) {
      yOffset = ((backgroundLayer.tileHeight << 3) - screen.screenImageData.height);
    }

    screen.setOffset(xOffset, yOffset);

    for (int y = (yOffset >> 3); y < (yOffset + screen.screenImageData.height >> 3) + 1; y++) {
      for (int x = (xOffset >> 3); x < (xOffset + screen.screenImageData.width >> 3) + 1; x++) {
        backgroundLayer.getTile(x, y).render(screen, backgroundLayer, x << 3, y << 3);
      }
    }
  }


  @Override
  public void render() {

    /* Render the backgroundLayer into the screen. */
    renderLevel(level.backgroundLayer, 0, 0);

    /* Render all the entities. */

    renderSpriteSystem.update(tickTime, level.getEntityContainer().stream());

    /* Render the screen into the framebuffer. */
    for (int y = 0; y < screen.screenImageData.height; y++) {
      for (int x = 0; x < screen.screenImageData.width; x++) {
        int colourCode = screen.screenImageData.pixels[x + y * screen.screenImageData.width];
        if (colourCode < 255) {
          frameBufferPixels[x + y * gameConfig.width] = rgbColorPalette.rgbValueForIndex(colourCode);
        }
      }
    }
  }

  /**
   * The current game time in ticks. A tick happens every frame of the desired TARGET_FPS.
   */
  @Override
  public int getTickTime() {
    return tickTime;
  }
}
