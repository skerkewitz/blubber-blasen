package de.skerkewitz.blubberblase.esc.component;

import de.skerkewitz.enora2d.core.ecs.component.Component;

import java.util.EnumSet;

public class CollisionComponent implements Component {

  private final EnumSet<Layer> isOnLayer;
  private final EnumSet<Layer> collideWithLayer;

  public CollisionComponent(EnumSet<Layer> isOnLayer, EnumSet<Layer> collideWithLayer) {
    this.isOnLayer = isOnLayer;
    this.collideWithLayer = collideWithLayer;
  }

  public boolean didCollide = false;

  public boolean doesCollideWith(CollisionComponent other) {
    EnumSet<Layer> clone = collideWithLayer.clone();
    clone.retainAll(other.isOnLayer);
    return !clone.isEmpty();
  }

  public boolean removeCollideWithLayer(Layer layer) {
    return collideWithLayer.remove(layer);
  }

  public enum Layer {
    PLAYER,
    BUBBLE,
    ENEMY
  }

  public void applyCollide(boolean didCollide) {
    if (!didCollide) {
      return;
    }

    this.didCollide = true;
  }
}
