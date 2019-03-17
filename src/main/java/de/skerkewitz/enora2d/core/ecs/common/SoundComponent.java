package de.skerkewitz.enora2d.core.ecs.common;

import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.enora2d.core.ecs.Component;

public class SoundComponent implements Component {

  public final Sound sound;
  public final float volume;
  public boolean shouldPlay = true;

  public SoundComponent(Sound sound, float volume) {
    this.sound = sound;
    this.volume = volume;
  }
}
