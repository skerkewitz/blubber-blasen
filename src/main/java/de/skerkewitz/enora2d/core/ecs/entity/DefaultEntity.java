package de.skerkewitz.enora2d.core.ecs.entity;

import de.skerkewitz.enora2d.core.ecs.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The actual entity implementation.
 */
public class DefaultEntity implements Entity {

  private final List<Component> components = new ArrayList<>();

  private boolean expired;

  /**
   * Add the given component to the list of component for this entity.
   *
   * @return true if the the component list has changed, else false.
   */
  @Override
  public boolean addComponent(Component component) {
    return components.add(component);
  }

  @Override
  public <T extends Component> T getComponent(Class<T> type) {

    for (var c : components) {
      if (type.isInstance(c)) {
        return type.cast(c);
      }
    }

    return null;
  }

  @Override
  public void expired() {
    expired = true;
  }

  @Override
  public final boolean isExpired() {
    return expired;
  }

  @Override
  public final boolean isAlive() {
    return !isExpired();
  }
}
