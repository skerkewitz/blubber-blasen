package de.skerkewitz.enora2d.core.game;

import de.skerkewitz.blubberblase.entity.Bubble;
import de.skerkewitz.enora2d.backend.awt.game.WindowHandler;
import de.skerkewitz.enora2d.core.entity.Entity;
import de.skerkewitz.enora2d.core.entity.Player;
import de.skerkewitz.enora2d.core.game.level.BackgroundLayer;
import de.skerkewitz.enora2d.core.game.level.Level;
import de.skerkewitz.enora2d.core.gfx.ImageData;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.gfx.Screen;
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

    level = new Level();

    player = new Player(4 * 8, 26 * 8, input);
    level.spawnEntity(player);
    level.spawnEntity(new Bubble("Bubble", 8 * 8, 24 * 8, 1));
  }


  @Override
  public synchronized void start() {
    running = true;

    thread = new Thread(this, gameConfig.name + "_main");
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
    long lastTime = System.nanoTime();
    double nsPerTick = 1000000000D / TARGET_FPS;

    int ticks = 0;
    int frames = 0;

    long lastTimer = System.currentTimeMillis();
    double delta = 0;

    try {
      init();
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize because of: " + e, e);
    }

    while (running) {
      long now = System.nanoTime();
      delta += (now - lastTime) / nsPerTick;
      lastTime = now;
      boolean shouldRender = true;

      while (delta >= 1) {
        ticks++;
        tick();
        delta -= 1;
        shouldRender = true;
      }

      try {
        Thread.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (shouldRender) {
        frames++;
        render();
      }

      if (System.currentTimeMillis() - lastTimer >= 1000) {
        lastTimer += 1000;
        logger.info("[" + gameConfig.name + "] " + (ticks + " ticks, " + frames + " frames"));
        frames = 0;
        ticks = 0;
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
    if (xOffset > ((backgroundLayer.tileWidth << 3) - screen.pixelWidth)) {
      xOffset = ((backgroundLayer.tileWidth << 3) - screen.pixelWidth);
    }
    if (yOffset < 0) {
      yOffset = 0;
    }
    if (yOffset > ((backgroundLayer.tileHeight << 3) - screen.pixelHeight)) {
      yOffset = ((backgroundLayer.tileHeight << 3) - screen.pixelHeight);
    }

    screen.setOffset(xOffset, yOffset);

    for (int y = (yOffset >> 3); y < (yOffset + screen.pixelHeight >> 3) + 1; y++) {
      for (int x = (xOffset >> 3); x < (xOffset + screen.pixelWidth >> 3) + 1; x++) {
        backgroundLayer.getTile(x, y).render(screen, backgroundLayer, x << 3, y << 3);
      }
    }
  }


  @Override
  public void render() {

    int xOffset = player.posX - (screen.pixelWidth / 2);
    int yOffset = player.posY - (screen.pixelHeight / 2);

    /* Render the backgroundLayer into the screen. */
    renderLevel(level.backgroundLayer, xOffset, yOffset);


    /* Render all the entities. */
    level.getEntityContainer().forEach((Entity e) -> e.render(screen));

    /* Render the screen into the framebuffer. */
    for (int y = 0; y < screen.pixelHeight; y++) {
      for (int x = 0; x < screen.pixelWidth; x++) {
        int colourCode = screen.pixels[x + y * screen.pixelWidth];
        if (colourCode < 255)
          frameBufferPixels[x + y * gameConfig.width] = rgbColorPalette.rgbValueForIndex(colourCode);
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
