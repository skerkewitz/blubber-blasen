package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.Objects;
import java.util.stream.Stream;

public class BaseComponentSystem<T extends ComponentSystem.Tuple, F extends ComponentSystem.TupleFactory<T>> implements ComponentSystem<T> {

  private F tupleFactory;

  public BaseComponentSystem(F tupleFactory) {
    this.tupleFactory = tupleFactory;
  }

  @Override
  public void update(int tickTime, World world, Stream<Entity> stream) {
    willExecute(tickTime, world);
    executor(tickTime, world, stream);
    didExecute(tickTime, world);
  }

  @Override
  public void executor(int tickTime, World world, Stream<Entity> stream) {
    getTuples(stream).forEach(tuple -> execute(tickTime, tuple, world));
  }

  @Override
  public void willExecute(int tickTime, World world) {

  }

  @Override
  public void execute(int tickTime, T t, World world) {

  }

  @Override
  public void didExecute(int tickTime, World world) {

  }

  @Override
  public Stream<T> getTuples(Stream<Entity> stream) {
    return stream.map(tupleFactory::map).filter(Objects::nonNull);
  }
}
