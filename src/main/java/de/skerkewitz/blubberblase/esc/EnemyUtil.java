package de.skerkewitz.blubberblase.esc;

public class EnemyUtil {

  public EnemyUtil() {
    /* No instance allowed. */
  }

  public static void setupDidEscapeTrapBubble(EnemyComponent enemyComponent) {
    enemyComponent.isAngry = true;
    enemyComponent.didEscapeTrapBubble = true;
  }
}
