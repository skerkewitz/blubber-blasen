package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.util.TimeUtil;

public interface ThrownEnemy {
  int KICKED_TIME = TimeUtil.secondsToTickTime(1.5f);
  int MAX_LIFETIME_IN_TICKS = TimeUtil.secondsToTickTime(10.0f);
}
