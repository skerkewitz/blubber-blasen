package de.skerkewitz.enora2d.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityContainer {

  private List<Entity> entities = new ArrayList<Entity>();
  private List<Entity> spawningEntities = new ArrayList<Entity>();

  public void purgeExpired() {
    /* Remove all expired entities. */
    entities = entities.stream().filter(Entity::isAlive).collect(Collectors.toList());
    entities.addAll(spawningEntities);
    spawningEntities.clear();
  }

  public void addEntity(Entity entity) {
    this.spawningEntities.add(entity);
  }

  public Optional<Entity> findFirst(Predicate<? super Entity> predicate) {
    return entities.stream().filter(predicate).findFirst();
  }

  public void forEach(Consumer<? super Entity> action) {
    entities.forEach(action);
  }
}
