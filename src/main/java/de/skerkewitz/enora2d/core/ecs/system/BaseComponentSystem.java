package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.enora2d.core.ecs.entity.Entity;

import java.util.Objects;
import java.util.stream.Stream;

public class BaseComponentSystem<T extends ComponentSystem.Tuple, F extends ComponentSystem.TupleFactory<T>> implements ComponentSystem<T> {

  private F tupleFactory;

  public BaseComponentSystem(F tupleFactory) {
    this.tupleFactory = tupleFactory;
  }

  @Override
  public void update(int tickTime, Stream<Entity> stream) {
    getTuples(stream).forEach(tuple -> execute(tickTime, tuple));
  }

  @Override
  public void execute(int tickTime, T t) {

  }

  @Override
  public Stream<T> getTuples(Stream<Entity> stream) {
    return stream.map(tupleFactory::map).filter(Objects::nonNull);
  }
}