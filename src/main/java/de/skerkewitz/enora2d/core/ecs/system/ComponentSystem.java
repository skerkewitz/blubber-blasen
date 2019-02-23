package de.skerkewitz.enora2d.core.ecs.system;

import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.game.level.Level;

import java.util.stream.Stream;

public interface ComponentSystem<T extends ComponentSystem.Tuple> {

  void update(int tickTime, Level level, Stream<Entity> stream);

  void execute(int tickTime, T t, Level level);

  Stream<T> getTuples(Stream<Entity> stream);

  interface Tuple {

  }

  interface TupleFactory<T> {

    T map(Entity entity);

  }

}
