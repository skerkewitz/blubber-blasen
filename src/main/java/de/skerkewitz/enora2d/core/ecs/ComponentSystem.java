package de.skerkewitz.enora2d.core.ecs;

import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.enora2d.core.game.world.World;

import java.util.stream.Stream;

public interface ComponentSystem<T extends ComponentSystem.Tuple> {

  void update(int tickTime, World world, Stream<Entity> stream, GameContext context);

  void executor(int tickTime, World world, Stream<Entity> stream, GameContext context);

  void willExecute(int tickTime, World world);

  void execute(int tickTime, T t, World world, GameContext context);

  void didExecute(int tickTime, World world);

  Stream<T> getTuples(Stream<Entity> stream);

  interface Tuple {

  }

  interface TupleFactory<T> {

    T map(Entity entity);

  }

}
