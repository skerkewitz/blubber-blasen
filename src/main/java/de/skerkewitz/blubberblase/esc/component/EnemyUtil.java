package de.skerkewitz.blubberblase.esc.component;

public class EnemyUtil {

  public EnemyUtil() {
    /* No instance allowed. */
  }

  public static void setupDidEscapeTrapBubble(EnemyComponent enemyComponent) {
    enemyComponent.isAngry = true;
    enemyComponent.didEscapeTrapBubble = true;
  }
}
