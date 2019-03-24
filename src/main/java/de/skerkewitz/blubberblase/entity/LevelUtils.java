package de.skerkewitz.blubberblase.entity;

import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import de.gierzahn.editor.map.EnemyBaseMapLayer;
import de.gierzahn.editor.map.Map;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.LevelScreenWorld;
import de.skerkewitz.blubberblase.esc.EnemyComponent;
import de.skerkewitz.blubberblase.esc.RenderTextComponent;
import de.skerkewitz.blubberblase.esc.StateBaseBubbleComponent;
import de.skerkewitz.blubberblase.esc.TargetMoveComponent;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.game.GameConfig;
import de.skerkewitz.enora2d.core.game.world.StaticMapContent;
import de.skerkewitz.enora2d.core.game.world.StaticMapContentLoader;
import de.skerkewitz.enora2d.core.game.world.World;
import de.skerkewitz.enora2d.core.game.world.tiles.Tile;
import de.skerkewitz.enora2d.core.input.GdxGamepadInputHandler;
import de.skerkewitz.enora2d.core.input.GdxKeyboardInputHandler;
import de.skerkewitz.enora2d.core.input.InputHandler;
import de.skerkewitz.enora2d.core.input.InputHandlerCombiner;

import java.util.ArrayList;
import java.util.stream.Stream;

public class LevelUtils {

  public interface TileWorld {
    Tile getTileAt(int tileX, int tileY);

    Tile getTileAtPosition(int worldX, int worldY);

    boolean isSolidAtPosition(int x, int y);
  }

  public static final int PLAYER1_SPAWN_X = 32;
  public static final int PLAYER1_SPAWN_Y = ((Map.MAX_DOWN) * 8) - 1;
  public static final Point2i PLAYER_SPAWN_POINT2I = new Point2i(PLAYER1_SPAWN_X, PLAYER1_SPAWN_Y);

  public static final Point2f PLAYER1_SPAWN_POINT2F = new Point2f(PLAYER1_SPAWN_X, PLAYER1_SPAWN_Y);
  public static final float MOVE_EPSILON = 0.0001f;

  public LevelUtils() {
    /* No instance allowed. */
  }

  public static World loadNextLevel(int tickTime, GameContext gameContext, GameConfig config, World previousWorld) {

    gameContext.currentLevelNum += 1;
    gameContext.clampLevelNum();

    return loadWorld(tickTime, config, gameContext.currentLevelNum, previousWorld);
  }

  public static World loadWorld(int frameCount, GameConfig config, int level, World previousWorld) {

    /* Load the map from file. */
    final StaticMapContent staticMapContent = StaticMapContentLoader.load(level);
    var world = new LevelScreenWorld(config, staticMapContent, frameCount);

    /* Create player score entity. */
    final Entity scoreEntity = EntityFactory.spawnTextEntity(new Point2f(0, 8), RenderTextComponent.Text.Empty, Color.WHITE);

    world.addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(3, 0), () -> "1UP", Color.GREEN));
    world.addEntity(EntityFactory.spawnTextEntity(LevelUtils.convertTileToWorldSpace(11, 0), () -> "HIGHSCORE", Color.RED));
    //world.addEntity(EntityFactory.spawnTextEntity(new Point2f(0,0), "1UP", Color.GREEN));

    final Entity playerEntity = createPlayerEntity(previousWorld);
    world.addPlayer(playerEntity, scoreEntity);

    ArrayList<EnemyBaseMapLayer.Enemy> enemySpawnList = staticMapContent.getEnemySpawnList();
    for (EnemyBaseMapLayer.Enemy enemy : enemySpawnList) {
      Entity entity = createEnemyEntity(frameCount, enemy);
      world.addEntity(entity);
    }

    return world;
  }

  private static Entity createEnemyEntity(int frameCount, EnemyBaseMapLayer.Enemy enemy) {
    final var invertPivot = ZenChan.spritePivotPoint.invert();
    final var spawnLocation = convertTileToWorldSpace(enemy.x, enemy.y);

    final Point2f spawnPosition = new Point2f(spawnLocation.x + invertPivot.x, 8);
    final Point2i targetPosition = new Point2i(spawnLocation.x + invertPivot.x, spawnLocation.y + invertPivot.y);
    final MoveDirection moveDirection = enemy.isLookingLeft ? MoveDirection.Left : MoveDirection.Right;
    final Entity entity = EntityFactory.spawnZenChan(spawnPosition, frameCount, moveDirection, false);
    entity.addComponent(new TargetMoveComponent(targetPosition, 1));
    return entity;
  }

  public static Point2f convertTileToWorldSpace(int x, int y) {
    return new Point2f(x << 3, y << 3);
  }

  private static Entity createPlayerEntity(World previousWorld) {

    final InputHandler handler;
    if (Controllers.getControllers().size > 0) {
      handler = new InputHandlerCombiner(new GdxKeyboardInputHandler(), new GdxGamepadInputHandler(Controllers.getControllers().first()));
    } else {
      handler = new GdxKeyboardInputHandler();
    }

    Point2f lastPosition = null;
    if (previousWorld != null) {
      lastPosition = previousWorld.getPlayerEntity().getComponent(TransformComponent.class).position;
    }

    Point2f playerSpawnPosition = (lastPosition == null) ? PLAYER1_SPAWN_POINT2F : lastPosition;
    Entity playerEntity = EntityFactory.spawnBubblun(handler, playerSpawnPosition);

    playerEntity.addComponent(new TargetMoveComponent(PLAYER_SPAWN_POINT2I, 1));

    return playerEntity;
  }

  public static boolean isLevelCleared(Stream<Entity> entityStream) {
    return entityStream
            .filter(Entity::isAlive)
            .noneMatch(entity -> {
              if (entity.hasComponent(EnemyComponent.class)) {
                return true;
              }

              StateBaseBubbleComponent component = entity.getComponent(StateBaseBubbleComponent.class);
              if (component != null) {
                return component.type == StateBaseBubbleComponent.Type.TRAP;
              }

              return false;
            });
  }

  public static boolean isGapInFront(int moveX, Point2f position, Rect2i boundingBox, World world) {

    /* If we don't move left or right then there is no gap. */
    if (moveX == 0) {
      return false;
    }

    final float ex;
    final float ey;
    if (moveX < 0) {
      ex = position.x - (boundingBox.size.width / 2.0f) + moveX;
      ey = position.y + 1;
    } else {
      ex = position.x + (boundingBox.size.width / 2.0f) + moveX;
      ey = position.y + 1;
    }

    /* if tile is not solid then there is a gap. */
    final Tile tile = world.getTileAtPosition((int) ex, (int) ey);
    return !tile.isSolid();
  }


  /**
   * @param moveY       the vertical move vector
   * @param position    the current position
   * @param boundingBox the bounding box of the moving object.
   * @param world       the tile world.
   * @return the clipped vertical move vector
   */

  public static int clipMoveY(float moveY, Point2f position, Rect2i boundingBox, TileWorld world) {

    /* The minimum amount of movement. */
    if (Math.abs(moveY) < MOVE_EPSILON) {
      return 0;
    }

    /* Up movement is always unrestricted. */
    if (moveY < 0) {
      return Math.round(moveY);
    }

    /* For down movement we need to find the intersection points of the tile grid. */

    /* First intersection point. */
    var iStartX = Math.round(position.x);
    var iStartY = Math.round(position.y);

    var startTileX = iStartX / 8;
    var startTileY = iStartY / 8;

    var isInSolid = world.getTileAt(startTileX, startTileY).isSolid();

    var iEndX = Math.round(position.x);
    var iEndY = Math.round(position.y + moveY);

    var endTileX = iEndX / 8;
    var endTileY = iEndY / 8;

    for (var curTileY = startTileY + 1; curTileY <= endTileY; curTileY += 1) {
      var tile = world.getTileAt(startTileX, curTileY);
      if (isInSolid && tile.isSolid()) {
        continue;
      }
      isInSolid = false;

      if (tile.isSolid()) {
        return ((curTileY * 8) - iStartY) - 1; // last minus 1 because we want to be on the tile above the solid tile we just found.
      }
    }


    return Math.round(moveY);
  }


  public static float clipMoveX(float moveX, Point2f position, Rect2i boundingBox, World world) {

    /* no horizontal movement. */
    if (Math.abs(moveX) < MOVE_EPSILON) {
      return 0;
    }

    Tile oldTile;
    Tile newTile;
    if (moveX < 0) {

      float ox = position.x - (boundingBox.size.width / 2);
      float oy = position.y;

      float ex = position.x - (boundingBox.size.width / 2) + moveX;
      float ey = position.y;

      oldTile = world.getTileAtPosition((int) ox, (int) oy);
      newTile = world.getTileAtPosition((int) ex, (int) ey);
    } else {
      float ox = position.x + (boundingBox.size.width / 2);
      float oy = position.y;

      float ex = position.x + (boundingBox.size.width / 2) + moveX;
      float ey = position.y;

      oldTile = world.getTileAtPosition((int) ox, (int) oy);
      newTile = world.getTileAtPosition((int) ex, (int) ey);
    }

    if (oldTile.isSolid()) {
      return moveX;
    }

    if (!newTile.isSolid()) {
      return moveX;
    }

    return 0;
  }

  public static boolean canGapJump(int horizontalMoveVector, Point2f position, Rect2i boundingBox, World world) {

    /* If we don't move left or right then there is no gap. */
    if (horizontalMoveVector == 0) {
      return false;
    }

    final int tileX;
    final int tileY;

    if (horizontalMoveVector < 0) {
      tileX = (int) (position.x - (boundingBox.size.width / 2) + horizontalMoveVector);
      tileY = (int) (position.y + 1);
    } else {
      tileX = (int) (position.x + (boundingBox.size.width / 2) + horizontalMoveVector);
      tileY = (int) (position.y + 1);
    }

    /* if tile is not solid then there is a gap. */
    return world.canGapJumpAtPosition(tileX, tileY, horizontalMoveVector);
  }

  public static void clipPositionToLevelBounds(Point2f position, Rect2i boundingBox) {

    var minX = (int) (position.x - (boundingBox.size.width / 2));
    var maxX = (int) (position.x + (boundingBox.size.width / 2));

    final var dw = 2 * StaticMapContent.TILE_WIDTH;

    final var leftLimit = dw;
    final var rightLimit = StaticMapContent.WIDTH - dw - 1;

    if (minX < leftLimit) {
      position.x += (leftLimit - minX);
    } else if (maxX > rightLimit) {
      position.x -= (maxX - rightLimit);
    }

    /* Wrap around if entity did fall through. */
    if (position.y > StaticMapContent.HEIGHT + dw) {
      position.y = -dw;
    }
  }

  public static boolean canJumpUp(Point2f position, World world) {

    final int tileX = (int) (position.x);
    final int tileY = (int) (position.y + 1);

    /* if tile is not solid then there is a gap. */
    return world.canJumpUp(tileX, tileY);
  }

  public static boolean checkGround(Point2i position, TileWorld world) {
    final boolean lastTile = world.isSolidAtPosition(position.x, position.y);
    final boolean newTile = world.isSolidAtPosition(position.x, position.y + 1);
    return !lastTile && newTile;
  }
}
