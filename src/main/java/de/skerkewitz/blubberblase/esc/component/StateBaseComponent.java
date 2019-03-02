package de.skerkewitz.blubberblase.esc.component;

public class StateBaseComponent<T> {

  /**
   * The tick time at which this state did begin.
   */
  public int stateBeginFrameCount;
  public T state;

  public StateBaseComponent(int stateBeginFrameCount, T state) {
    this.stateBeginFrameCount = stateBeginFrameCount;
    this.state = state;
  }

  public void setState(int frameCount, T state) {
    this.stateBeginFrameCount = frameCount;
    this.state = state;
  }

  public int getStateAge(int frameCount) {
    return frameCount - stateBeginFrameCount;
  }
}
