package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.esc.EnemyComponent;
import de.skerkewitz.blubberblase.esc.StateBaseBubbleComponent;
import de.skerkewitz.enora2d.core.ecs.Entity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelUtilsTest {

  @Test
  void isLevelCleared_empty() {
    final List<Entity> entityList = new ArrayList<>();
    assertTrue(LevelUtils.isLevelCleared(entityList.stream()));
  }

  @Test
  void isLevelCleared_withEnemy() {

    final List<Entity> entityList = new ArrayList<>();
    var entity = EntityFactory.newEntity();
    entity.addComponent(new EnemyComponent(false));
    entityList.add(entity);

    assertFalse(LevelUtils.isLevelCleared(entityList.stream()));
  }

  @Test
  void isLevelCleared_withTrapBubble() {

    {
      final List<Entity> entityList = new ArrayList<>();
      var entity = EntityFactory.newEntity();
      entity.addComponent(new StateBaseBubbleComponent(0, StateBaseBubbleComponent.State.FLOAT, StateBaseBubbleComponent.Type.TRAP, false));
      entityList.add(entity);

      assertFalse(LevelUtils.isLevelCleared(entityList.stream()));
    }

    {
      final List<Entity> entityList = new ArrayList<>();
      var entity = EntityFactory.newEntity();
      entity.addComponent(new StateBaseBubbleComponent(0, StateBaseBubbleComponent.State.FLOAT, StateBaseBubbleComponent.Type.NORMAL, false));
      entityList.add(entity);

      var entity2 = EntityFactory.newEntity();
      entity2.addComponent(new StateBaseBubbleComponent(0, StateBaseBubbleComponent.State.FLOAT, StateBaseBubbleComponent.Type.TRAP, false));
      entityList.add(entity2);

      assertFalse(LevelUtils.isLevelCleared(entityList.stream()));
    }

  }
}