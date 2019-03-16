package de.skerkewitz.blubberblase.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.SpriteSource;

public interface Bubble {

  Sound sfxBurstTrapBubble = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_coin_double7.wav"));
  Sound sfxBurstBubble = Gdx.audio.newSound(Gdx.files.internal("sfx/bubble-burst.wav"));

  int BURST_MAX_LIFETIME_IN_TICKS = TimeUtil.secondsToTickTime(0.5);

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.1);
  int FRAME_ANIMATION_SPEED_RND_OFFSET = TimeUtil.secondsToTickTime(0.1);

  int MAX_LIFETIME_BEFORE_BURST = TimeUtil.secondsToTickTime(10);

  Animation BUBBLE = new Animation("bubble", FRAME_ANIMATION_SPEED,
          new SpriteSource(Ressources.SpriteSheet_Bubble.sheet.rectFor(0, 0), Ressources.SpriteSheet_Bubble),
          new SpriteSource(Ressources.SpriteSheet_Bubble.sheet.rectFor(1, 0), Ressources.SpriteSheet_Bubble),
          new SpriteSource(Ressources.SpriteSheet_Bubble.sheet.rectFor(2, 0), Ressources.SpriteSheet_Bubble)
  );


  float FLOATING_SPEED = 0.35f;
  float TRAPPED_SPEED = 0.3f;
}
