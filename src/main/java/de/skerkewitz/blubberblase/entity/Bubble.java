package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;

public interface Bubble {

  int COLOR_PALETTE = RgbColorPalette.mergeColorCodes(RgbColorPalette.NONE, RgbColorPalette.GREEN, RgbColorPalette.NONE, RgbColorPalette.WHITE);

  int MAX_LIFETIME_IN_TICKS = 300;

  int FRAME_ANIMATION_SPEED = TimeUtil.secondsToTickTime(0.25);

  int MAX_LIFETIME_BEFORE_BURST = TimeUtil.secondsToTickTime(5);
}
