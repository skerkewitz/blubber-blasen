package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.game.level.World;

import java.util.stream.Stream;

public interface ComponentSystem<T extends ComponentSystem.Tuple> {

  void update(int tickTime, World world, Stream<Entity> stream);

  void willExecute(int tickTime, World world);

  void execute(int tickTime, T t, World world);

  void didExecute(int tickTime, World world);

  Stream<T> getTuples(Stream<Entity> stream);

  interface Tuple {

  }

  interface TupleFactory<T> {

    T map(Entity entity);

  }

}
