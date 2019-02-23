package de.skerkewitz.enora2d.core.ecs.entity;

import de.skerkewitz.enora2d.core.ecs.component.Component;

public interface Entity {

  boolean isAlive();

  boolean isExpired();

  boolean addComponent(Component component);

  <T extends Component> T getComponent(Class<T> type);

  void expired();
}
