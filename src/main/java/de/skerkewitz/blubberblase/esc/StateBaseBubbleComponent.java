package de.skerkewitz.blubberblase.esc;

import de.skerkewitz.enora2d.core.ecs.Component;

public class StateBaseBubbleComponent extends StateBaseComponent<StateBaseBubbleComponent.State> implements Component {

  public Type type;
  public boolean isAngry;

  public StateBaseBubbleComponent(int stateBeginTime, State state, Type type, boolean angry) {
    super(stateBeginTime, state);
    this.type = type;
    this.isAngry = angry;
  }
  public enum State {
    SHOOT,
    FLOAT
  }

  public enum Type {
    NORMAL,
    TRAP
  }


}
