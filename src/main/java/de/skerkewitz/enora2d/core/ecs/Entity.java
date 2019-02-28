package de.skerkewitz.enora2d.core.ecs;

public interface Entity {

//  class ObjectAttributes {
//
//    int size = 1; // 1 means 8x8, 2 means 16x16
//    //int tileIndex;
//
//  }
//
//  // Size (8x8, 16x16), pos, prio, flipv, fliph, first tile, color palette

  boolean isAlive();

  boolean isExpired();

  boolean addComponent(Component component);

  <T extends Component> T getComponent(Class<T> type);

  void expired();

  <T extends Component> boolean hasComponent(Class<T> type);
}
