package de.skerkewitz.enora2d.core.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityContainer {

  private List<Entity> entities = new ArrayList<>();
  private List<Entity> spawningEntities = new ArrayList<>();

  public void purgeExpired() {
    /* Remove all expired entities. */
    entities = entities.stream().filter(Entity::isAlive).collect(Collectors.toList());
    entities.addAll(spawningEntities);
    spawningEntities.clear();
  }

  public void addEntity(Entity legacyEntity) {
    this.spawningEntities.add(legacyEntity);
  }

  public Optional<Entity> findFirst(Predicate<? super Entity> predicate) {
    return entities.stream().filter(predicate).findFirst();
  }

  public Stream<Entity> stream() {
    return entities.stream();
  }

  public void forEach(Consumer<? super Entity> action) {
    entities.forEach(action);
  }
}
