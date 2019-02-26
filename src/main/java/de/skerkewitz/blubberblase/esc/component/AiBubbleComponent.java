package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;

public class AiBubbleComponent extends AiComponent<AiBubbleComponent.State> implements Component {

  public Type type;
  public boolean isAngry;

  public AiBubbleComponent(int stateBeginTime, State state, Type type, boolean angry) {
    super(stateBeginTime);
    this.state = state;
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
