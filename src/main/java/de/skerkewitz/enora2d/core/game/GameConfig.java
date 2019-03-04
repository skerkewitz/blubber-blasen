package de.skerkewitz.enora2d.core.game;

import org.apache.commons.cli.*;

import java.awt.*;

public class GameConfig {

  public static final String CMD_OPTION_SHOWBBOX = "showbbox";
  public static final String CMD_OPTION_DISABLEPPFX = "nopostfx";
  public static final String CMD_OPTION_LEVEL = "level";
  public static final String CMD_OPTION_NONEXTLEVEL = "nonextlevel";

  public final String name;
  public final int width;
  public final int height;

  public final int scale;

  public final Dimension displayDimensions;


  public final boolean noNextLevel;


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
    options.addOption(CMD_OPTION_NONEXTLEVEL, false, "do not go to next level");
    options.addOption(CMD_OPTION_LEVEL, true, "specify start level");

    CommandLineParser parser = new DefaultParser();
    cmd = parser.parse(options, args);

    noNextLevel = cmd.hasOption(CMD_OPTION_NONEXTLEVEL);
  }
}
