package de.skerkewitz.blubberblase.screen.highscore;

import de.skerkewitz.blubberblase.esc.RenderTextComponent;
import org.apache.commons.lang3.StringUtils;

class HighscoreText implements RenderTextComponent.Text {

  final static String[] places = {"1ST", "2ND", "3RD", "4TH", "5TH"};

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

    return places[place]
            + "  "
            + StringUtils.leftPad("" + score, 6, ' ')
            + "  "
            + StringUtils.leftPad("" + round, 2, ' ')
            + "  "
            + StringUtils.leftPad("" + name, 10, '.');
  }
}
