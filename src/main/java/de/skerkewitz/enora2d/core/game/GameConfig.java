package de.skerkewitz.enora2d.core.game;

import org.apache.commons.cli.*;

import java.awt.*;

public class GameConfig {

  public final String name;
  public final int width;
  public final int height;

  public final int scale;

  public final Dimension displayDimensions;


  public final CommandLine cmd;

  public GameConfig(int width, int height, int scale, String name, String[] args) throws ParseException {
    this.width = width;
    this.height = height;
    this.scale = scale;
    this.name = name;

    displayDimensions = new Dimension(width * scale, height * scale);

    final Options options = new Options();
    options.addOption("showbbox", false, "show bounding boxes");

    CommandLineParser parser = new DefaultParser();
    cmd = parser.parse(options, args);
  }
}
