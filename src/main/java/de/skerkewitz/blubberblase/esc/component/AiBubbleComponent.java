package de.skerkewitz.blubberblase.esc.component;

public class AiBubbleComponent extends AiComponent {

  /**
   * The current state.
   */
  public State currentState;

  public AiBubbleComponent(int stateBeginTime, State currentState) {
    this.stateBeginTime = stateBeginTime;
    this.currentState = currentState;
  }

  public void setState(int tickTime, State state) {
    this.stateBeginTime = tickTime;
    this.currentState = state;
  }

  public enum State {
    SHOOT,
    FLOAT
  }
}
