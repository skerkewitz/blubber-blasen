package de.skerkewitz.enora2d.backend.awt.game;

import de.skerkewitz.enora2d.backend.awt.input.KeyboardInputHandler;
import de.skerkewitz.enora2d.core.game.AbstractGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class AwtGame extends AbstractGame {

  private static final Logger logger = LogManager.getLogger(AwtGame.class);


  JFrame frame;

  private BufferedImage image;

  /* Post processing scan line effect. */
  private final int scanlineEffect = 4;
  /**
   * Post effects blur matrix.
   */
  private float[] blurMatrix = new float[]{
          1f / 16f, 1f / 8f, 1f / 16f,
          1f / 8f, 1f / 16f, 1f / 8f,
          1f / 16f, 1f / 8f, 1f / 16f
  };
  private Kernel blurKernel = new Kernel(3, 3, blurMatrix);
  private ConvolveOp blurConvolveOp = new ConvolveOp(blurKernel, ConvolveOp.EDGE_NO_OP, null);

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

    final Graphics2D g = (Graphics2D) bs.getDrawGraphics();

    int screenWidth = getWidth();
    int screenHeight = getHeight();
    int upScaledWith = image.getWidth() * 4;
    int upScaledHeight = image.getHeight() * 4;
    BufferedImage upScaleImage = new BufferedImage(upScaledWith, upScaledHeight, BufferedImage.TYPE_INT_RGB);
    upScaleImage.getGraphics().drawImage(image, 0, 0, upScaledWith, upScaledHeight, null);

    BufferedImage blurredImage = new BufferedImage(upScaledWith, upScaledHeight, BufferedImage.TYPE_INT_RGB);
    blurConvolveOp.filter(upScaleImage, blurredImage);
    g.drawImage(blurredImage, 0, 0, screenWidth, screenHeight, null);

    /* Render TV lines. */
    g.setColor(new Color(0, 0, 0, 16));
    int scanlineOffset = (getTickTime() / 2) % scanlineEffect;
    for (int y = scanlineOffset; y < screenHeight; y += scanlineEffect) {
      g.drawLine(0, y, screenWidth, y);
    }

    g.dispose();
    bs.show();
    Toolkit.getDefaultToolkit().sync();
  }
}
