package de.skerkewitz.enora2d.backend.awt.game;

import de.skerkewitz.enora2d.backend.awt.input.KeyboardInputHandler;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class AwtGame extends AbstractGame {

  private static final Logger logger = LogManager.getLogger(AwtGame.class);

  JFrame frame;

  private BufferedImage image;

  public AwtGame(GameConfig config) {
    super(config);

    image = new BufferedImage(gameConfig.width, gameConfig.height, BufferedImage.TYPE_INT_RGB);
  }

  public int[] getFrameBufferPixel() {
    return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
  }

  @Override
  public synchronized void start() {
    initBackend();
    super.start();
  }

  public void initBackend() {
    setMinimumSize(gameConfig.displayDimensions);
    setMaximumSize(gameConfig.displayDimensions);
    setPreferredSize(gameConfig.displayDimensions);

    frame = new JFrame(gameConfig.name);
    System.setProperty("sun.java2d.trace", "count");

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    frame.add(this, BorderLayout.CENTER);
    frame.pack();

    frame.setResizable(true);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    windowHandler = new WindowHandler(this);

    input = new KeyboardInputHandler(this);
  }

  @Override
  public void render() {

    /* Render the game into the framebuffer. */
    super.render();


    /* Render the frame buffer to screen. */
    BufferStrategy bs = getBufferStrategy();
    if (bs == null) {
      createBufferStrategy(3);
      return;
    }

    Graphics g = bs.getDrawGraphics();
    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    g.dispose();
    bs.show();
    Toolkit.getDefaultToolkit().sync();
//    logger.info("Flip");
  }
}
