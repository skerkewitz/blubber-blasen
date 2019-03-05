package de.skerkewitz.enora2d.core.ecs;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.Objects;
import java.util.stream.Stream;

public abstract class BaseComponentSystem<T extends ComponentSystem.Tuple, F extends ComponentSystem.TupleFactory<T>> implements ComponentSystem<T> {

  private F tupleFactory;

  public BaseComponentSystem(F tupleFactory) {
    this.tupleFactory = tupleFactory;
  }

  @Override
  public boolean update(int tickTime, World world, Stream<Entity> stream, GameContext context) {
    willExecute(tickTime, world);
    boolean didProcessAnything = executor(tickTime, world, stream, context);
    didExecute(tickTime, world, didProcessAnything);
    return didProcessAnything;
  }

  @Override
  public boolean executor(int tickTime, World world, Stream<Entity> stream, GameContext context) {
    return getTuples(stream).map(tuple -> invokeExecute(tickTime, tuple, world, context)).count() > 0;
  }

  private boolean invokeExecute(int tickTime, T tuple, World world, GameContext context) {
    execute(tickTime, tuple, world, context);
    return true;
  }

  @Override
  public void willExecute(int tickTime, World world) {

  }

  @Override
  public abstract void execute(int tickTime, T t, World world, GameContext context);

  @Override
  public void didExecute(int tickTime, World world, boolean didProcessAnything) {

  }

  @Override
  public Stream<T> getTuples(Stream<Entity> stream) {
    return stream.map(tupleFactory::map).filter(Objects::nonNull);
  }
}
