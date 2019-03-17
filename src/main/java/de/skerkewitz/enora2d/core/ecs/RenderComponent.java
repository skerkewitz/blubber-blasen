package de.skerkewitz.enora2d.core.ecs;

/**
 * The interface for all components.
 */
public interface RenderComponent extends Component {

  float getAlpha();

  void setAlpha(float alpha);

  byte getPriority();

  void setPriority(byte priority);

  boolean isVisible();

  void setVisible(boolean visible);

}
