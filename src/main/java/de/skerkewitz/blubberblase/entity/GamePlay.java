package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.util.TimeUtil;

public interface GamePlay {

  /**
   * How much time should the next action be indicated before it will be done.
   */
  int PRE_ACTION_INDICATION_FRAMECOUNT = TimeUtil.secondsToTickTime(1.5f);

}
