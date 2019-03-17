package de.skerkewitz.enora2d.core.ecs;

/**
 * The interface for all components.
 */
public abstract class AbstractRenderComponent implements RenderComponent {

  /**
   * Render priority, lower get rendered first therefore higher components are in front.
   */
  private byte priority = 0;

  private boolean visible = true;

  private float alpha = 1.0f;

  @Override
  public byte getPriority() {
    return this.priority;
  }

  @Override
  public void setPriority(byte priority) {
    this.priority = priority;
  }

  @Override
  public boolean isVisible() {
    return this.visible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public float getAlpha() {
    return this.alpha;
  }

  @Override
  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }
}
