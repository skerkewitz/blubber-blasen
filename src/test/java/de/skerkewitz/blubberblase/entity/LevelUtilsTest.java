package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.esc.EnemyComponent;
import de.skerkewitz.blubberblase.esc.StateBaseBubbleComponent;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.game.world.tiles.BasicSolidTile;
import de.skerkewitz.enora2d.core.game.world.tiles.BasicTile;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

  static class TileWorldIOmpl implements LevelUtils.TileWorld {

    private int[] data = {
            1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 1, 0, 1,
            1, 0, 0, 0, 0, 1, 1, 0, 1,
            1, 0, 0, 0, 0, 0, 1, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1,
    };

    @Override
    public Tile getTileAtPosition(int worldX, int worldY) {
      return getTileAt(worldX / 8, worldY / 8);
    }

    @Override
    public Tile getTileAt(int tileX, int tileY) {
      var t = data[(tileY * 9) + tileX];
      if (t == 1) {
        return new BasicSolidTile();
      }

      return new BasicTile();
    }

    @Override
    public boolean isSolidAtPosition(int worldX, int worldY) {
      return getTileAtPosition(worldX, worldY).isSolid();
    }
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

  @Test
  void clipMoveY_testEpsilon() {

    int i = LevelUtils.clipMoveY(0.000001f, null, null, null);
    assertEquals(0, i);
  }

  @Test
  void clipMoveY_testUpmove() {

    /* Up movement is unrestricted. */
    assertEquals(0, LevelUtils.clipMoveY(-0.1f, null, null, null));
    assertEquals(0, LevelUtils.clipMoveY(-0.4f, null, null, null));
    assertEquals(-1, LevelUtils.clipMoveY(-0.8f, null, null, null));
    assertEquals(-1, LevelUtils.clipMoveY(-1f, null, null, null));
    assertEquals(-10, LevelUtils.clipMoveY(-10f, null, null, null));
    assertEquals(-100, LevelUtils.clipMoveY(-100f, null, null, null));
  }

  @Test
  void clipMoveY_testDownMove() {

    var pos = new Point2f(5 * 8, 12);

    /* Up movement is unrestricted. */
    assertEquals(0, LevelUtils.clipMoveY(0.1f, pos, null, new TileWorldIOmpl()));
    assertEquals(0, LevelUtils.clipMoveY(0.4f, pos, null, new TileWorldIOmpl()));
    assertEquals(1, LevelUtils.clipMoveY(0.8f, pos, null, new TileWorldIOmpl()));
    assertEquals(1, LevelUtils.clipMoveY(1f, pos, null, new TileWorldIOmpl()));

    assertEquals(3, LevelUtils.clipMoveY(10f, pos, null, new TileWorldIOmpl()));
    assertEquals(3, LevelUtils.clipMoveY(20f, pos, null, new TileWorldIOmpl()));
  }

  @Test
  void clipMoveY_testDownMoveStartFromSolid() {

    var pos = new Point2f(5 * 8, 4);

    /* Up movement is unrestricted. */
    assertEquals(0, LevelUtils.clipMoveY(0.1f, pos, null, new TileWorldIOmpl()));
    assertEquals(0, LevelUtils.clipMoveY(0.4f, pos, null, new TileWorldIOmpl()));
    assertEquals(1, LevelUtils.clipMoveY(0.8f, pos, null, new TileWorldIOmpl()));
    assertEquals(1, LevelUtils.clipMoveY(1f, pos, null, new TileWorldIOmpl()));

    assertEquals(10, LevelUtils.clipMoveY(10f, pos, null, new TileWorldIOmpl()));
    assertEquals(11, LevelUtils.clipMoveY(20f, pos, null, new TileWorldIOmpl()));

    assertTrue(LevelUtils.checkGround(new Point2i(pos.x, pos.y + 11), new TileWorldIOmpl()));

  }

  @Test
  void clipMoveY_testDownMoveAllSolid() {

    var pos = new Point2f(6 * 8, 4);

    /* Up movement is unrestricted. */
//    assertEquals(0, LevelUtils.clipMoveY(0.1f, pos, null, new TileWorldIOmpl()));
//    assertEquals(0, LevelUtils.clipMoveY(0.4f, pos, null, new TileWorldIOmpl()));
//    assertEquals(1, LevelUtils.clipMoveY(0.8f, pos, null, new TileWorldIOmpl()));
//    assertEquals(1, LevelUtils.clipMoveY(1f, pos, null, new TileWorldIOmpl()));

    assertEquals(10, LevelUtils.clipMoveY(10f, pos, null, new TileWorldIOmpl()));
    assertEquals(20, LevelUtils.clipMoveY(20f, pos, null, new TileWorldIOmpl()));
  }
}