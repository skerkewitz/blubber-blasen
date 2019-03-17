package de.skerkewitz.blubberblase.screen.highscore;

import de.skerkewitz.blubberblase.esc.RenderTextComponent;
import org.apache.commons.lang3.StringUtils;

public class HighscoreText implements RenderTextComponent.Text {

  public int score;
  int round;
  String name;
  int place;

  public HighscoreText(int place, int score, int round, String name) {
    this.place = place;
    this.score = score;
    this.round = round;
    this.name = name;
  }

  @Override
  public String getText() {

    return StringUtils.leftPad("" + (place + 1), 2, ' ') + "."
            + "  "
            + StringUtils.leftPad("" + score, 6, ' ')
            + "  "
            + StringUtils.leftPad("" + round, 2, ' ')
            + "  "
            + StringUtils.leftPad("" + name, 10, '.');
  }
}
