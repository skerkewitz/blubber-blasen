package de.skerkewitz.blubberblase.esc.component;

public class AiComponent<T> {

  /**
   * The tick time at which this state did begin.
   */
  public int stateBeginTime;
  public T state;

  public AiComponent(int stateBeginTime) {
    this.stateBeginTime = stateBeginTime;
  }

  public void setState(int tickTime, T state) {
    this.stateBeginTime = tickTime;
    this.state = state;
  }

  public int getStateTime(int tickTime) {
    return tickTime - stateBeginTime;
  }
}
