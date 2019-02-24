package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;

public class AiComponent implements Component {

  /**
   * The tick time at which this state did begin.
   */
  public int stateBeginTime;

  public int getStateTime(int tickTime) {
    return tickTime - stateBeginTime;
  }
}
