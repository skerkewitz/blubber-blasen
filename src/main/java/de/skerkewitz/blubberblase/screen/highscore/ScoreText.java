package de.skerkewitz.blubberblase.screen.highscore;

import de.skerkewitz.blubberblase.esc.RenderTextComponent;
import org.apache.commons.lang3.StringUtils;

public class ScoreText implements RenderTextComponent.Text {

  public int score;

  public ScoreText(int score) {
    this.score = score;
  }

  @Override
  public String getText() {
    return StringUtils.center("" + score, 10);
  }
}
