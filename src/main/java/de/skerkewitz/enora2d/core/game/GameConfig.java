package de.skerkewitz.enora2d.core.game;

import org.apache.commons.cli.*;

import java.awt.*;

public class GameConfig {

  public static final String CMD_OPTION_SHOWBBOX = "showbbox";
  public static final String CMD_OPTION_DISABLEPPFX = "disableppfx";
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
    options.addOption(CMD_OPTION_SHOWBBOX, false, "show bounding boxes");
    options.addOption(CMD_OPTION_DISABLEPPFX, false, "disable post processing fx");

    CommandLineParser parser = new DefaultParser();
    cmd = parser.parse(options, args);
  }
}
