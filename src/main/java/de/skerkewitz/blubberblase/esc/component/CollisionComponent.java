package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CollisionComponent implements Component {

  private final Set<Entity> collisionSet = new HashSet<>();

  private final EnumSet<Layer> isOnLayer;
  private final EnumSet<Layer> collideWithLayer;

  public void addCollide(Entity o) {
    collisionSet.add(o);
  }

  public CollisionComponent(EnumSet<Layer> isOnLayer, EnumSet<Layer> collideWithLayer) {
    this.isOnLayer = isOnLayer;
    this.collideWithLayer = collideWithLayer;
  }

  public void clearCollisionSet() {
    collisionSet.clear();
  }

  public Stream<Entity> getCollisions() {
    return collisionSet.stream();
  }

  public boolean hasCollission() {
    return !collisionSet.isEmpty();
  }

  public boolean canCollideWith(CollisionComponent other) {
    EnumSet<Layer> clone = collideWithLayer.clone();
    clone.retainAll(other.isOnLayer);
    return !clone.isEmpty();
  }

  public enum Layer {
    PLAYER,
    BUBBLE,
    TRAP_BUBBLE,
    ENEMY
  }

  public boolean removeCollideWithLayer(Layer layer) {
    return collideWithLayer.remove(layer);
  }
}
