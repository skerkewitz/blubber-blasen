package de.skerkewitz.enora2d.core.ecs;

public interface Entity {

  boolean isAlive();

  boolean isExpired();

  boolean addComponent(Component component);

  <T extends Component> T getComponent(Class<T> type);

  void expired();

  <T extends Component> boolean hasComponent(Class<T> type);

  void removeComponent(Component component);
}
