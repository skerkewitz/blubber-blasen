package de.skerkewitz.blubberblase.esc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.blubberblase.entity.ZenChan;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Dice;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.game.world.World;

import static de.skerkewitz.blubberblase.esc.AiEnemySystem.AiEnemyUtils.RelativePlayerHeight.Above;
import static de.skerkewitz.blubberblase.esc.AiEnemySystem.AiEnemyUtils.RelativePlayerHeight.Below;

/**
 * A common to render all SpriteComponents.
 */
public class AiEnemySystem extends BaseComponentSystem<AiEnemySystem.Tuple, AiEnemySystem.TupleFactory> {

  private Sound sfxBurstTrapBubble = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_coin_double7.wav"));

  public AiEnemySystem() {
    super(new TupleFactory());
  }

  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    final TransformComponent transformComponent = t.transformComponent;
    final BoundingBoxComponent boundingBoxComponent = t.entity.getComponent(BoundingBoxComponent.class);

    int moveVectorX = 0;
    int moveVectorY = 0;

    final AiEnemyUtils.RelativePlayerHeight playerRelativeYPosition = AiEnemyUtils.relativePlayerHeightPosition(t.entity, world.getPlayerEntity());

    var playerMoveDirection = t.movementComponent.currentMoveDirection;
    final Rect2i boundingBox = boundingBoxComponent.getBoundingBox();

    boolean isOnGround = t.entity.getComponent(GroundDataComponent.class).isOnGround;
    if (t.enemyComponent.jumpTickRemaining > 0) {
      t.enemyComponent.jumpTickRemaining -= 1;
      moveVectorY -= 1;

      /* In a gap jump we need to move horizontal. */
      if (t.enemyComponent.gapJump) {
        moveVectorX = LevelUtils.clipMoveX(playerMoveDirection.getHorizontalMoveVector(), transformComponent.position, boundingBox, world);
      }

    } else {
      if (isOnGround) {
        t.enemyComponent.gapJump = false;

        /* There is a 50/50 change every 5s that he will jump if possible. */
        if (tickTime % TimeUtil.TICKTIME_2s == 0 && playerRelativeYPosition == Above
                && LevelUtils.canJumpUp(transformComponent.position, world) && Dice.chance(0.4f)) {
          t.enemyComponent.jumpTickRemaining = EnemyComponent.JUMP_HEIGHT_IN_PIXEL;
        } else {

          /* On ground, check for gap. */
          if (LevelUtils.isGapInFront(playerMoveDirection.getHorizontalMoveVector(), transformComponent.position, boundingBox, world)) {
            if ((playerRelativeYPosition == Below && Dice.chance(0.8f)) || Dice.chance(0.1f)) {
              t.enemyComponent.walkOverEdge = true;
            }

            /* If we should no walk over the edge, then handle the gab. */
            if (!t.enemyComponent.walkOverEdge) {
              if (LevelUtils.canGapJump(playerMoveDirection.getHorizontalMoveVector(), transformComponent.position, boundingBox, world) && Dice.chance(0.25f)) {
                t.enemyComponent.jumpTickRemaining = EnemyComponent.JUMP_HEIGHT_IN_PIXEL_GAP_JUMP;
                t.enemyComponent.gapJump = true;
              } else {
                playerMoveDirection = playerMoveDirection.flipHorizontal();
              }
            }
          }

          /* Check for wall hit. */
          if (LevelUtils.clipMoveX(playerMoveDirection.getHorizontalMoveVector(), transformComponent.position, boundingBox, world) == 0) {
            playerMoveDirection = playerMoveDirection.flipHorizontal();
            t.enemyComponent.walkOverEdge = false;
          }

          /* Do the actual move. */
          moveVectorX = LevelUtils.clipMoveX(playerMoveDirection.getHorizontalMoveVector(), transformComponent.position, boundingBox, world);
        }
      } else {
        /* In air, not in jump, fall down. */
        moveVectorY += 1;
        t.enemyComponent.walkOverEdge = false;

        /* In a gap jump we need to move horizontal. */
        if (t.enemyComponent.gapJump) {
          moveVectorX = LevelUtils.clipMoveX(playerMoveDirection.getHorizontalMoveVector(), transformComponent.position, boundingBox, world);
        }
      }
    }

    /* Update player position. */
    Point2f position = transformComponent.position;
    position.x += moveVectorX * (t.enemyComponent.speed + (t.enemyComponent.isAngry ? 0.3f : 0.0f));
    position.y += moveVectorY * t.enemyComponent.speed;

    LevelUtils.clipPositionToLevelBounds(transformComponent.position, boundingBox);

    t.movementComponent.currentMoveDirection = playerMoveDirection;

    if (t.enemyComponent.isAngry) {
      t.entity.getComponent(AnimationComponent.class).animation = ZenChan.ANGRY_ANIMATION_IDLE;
      t.entity.getComponent(RenderSpriteComponent.class).colorPalette = ZenChan.ANGRY_COLOR_PALETTE;
    }

    if (t.enemyComponent.didEscapeTrapBubble) {
      t.entity.getComponent(RenderSpriteComponent.class).colorPalette = ZenChan.ESCAPE_COLOR_PALETTE;
    }

    AnimationComponent animationComponent = t.entity.getComponent(AnimationComponent.class);
    animationComponent.flipX = t.movementComponent.currentMoveDirection == MoveDirection.Right;
  }

  /**
   * Declares the component needed by this common.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    EnemyComponent enemyComponent;
    TransformComponent transformComponent;
    MovementComponent movementComponent;

    Tuple(Entity entity, TransformComponent transformComponent, MovementComponent movementComponent, EnemyComponent enemyComponent) {
      this.entity = entity;
      this.transformComponent = transformComponent;
      this.movementComponent = movementComponent;
      this.enemyComponent = enemyComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var movementComponent = entity.getComponent(MovementComponent.class);
      var transformComponent = entity.getComponent(TransformComponent.class);
      var enemyComponent = entity.getComponent(EnemyComponent.class);
      if (movementComponent != null && transformComponent != null && enemyComponent != null) {
        return new Tuple(entity, transformComponent, movementComponent, enemyComponent);
      } else {
        return null;
      }
    }
  }

  public static class AiEnemyUtils {

    /**
     * Returns the height of the player in relation to the enemy.
     *
     * @param self
     * @param player
     * @return
     */
    public static RelativePlayerHeight relativePlayerHeightPosition(Entity self, Entity player) {

      var selfTransform = self.getComponent(TransformComponent.class);
      var playerTransform = player.getComponent(TransformComponent.class);

      if (selfTransform.position.y > (playerTransform.position.y + 16)) {
        return Above;
      } else if (selfTransform.position.y < (playerTransform.position.y - 32)) {
        return Below;
      }

      return RelativePlayerHeight.Same;
    }

    enum RelativePlayerHeight {
      Above,
      Same,
      Below
    }
  }
}