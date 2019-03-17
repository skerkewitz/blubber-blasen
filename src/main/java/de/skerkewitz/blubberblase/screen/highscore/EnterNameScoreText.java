package de.skerkewitz.blubberblase.screen.highscore;

import de.skerkewitz.blubberblase.esc.RenderTextComponent;
import org.apache.commons.lang3.StringUtils;

class EnterNameScoreText implements RenderTextComponent.Text {

  public int score;
  int round;
  String name;

  public EnterNameScoreText(int score, int round) {
    this.score = score;
    this.round = round;
    this.name = "";
  }

  @Override
  public String getText() {

    return StringUtils.leftPad("" + score, 6, ' ')
            + "  "
            + StringUtils.leftPad("" + round, 2, ' ')
            + "  "
            + StringUtils.leftPad("" + name, 10, '.');
  }
}
