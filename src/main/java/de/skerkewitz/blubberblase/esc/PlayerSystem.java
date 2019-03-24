package de.skerkewitz.blubberblase.esc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.Bubblun;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A common to render all SpriteComponents.
 */
public class PlayerSystem extends BaseComponentSystem<PlayerSystem.Tuple, PlayerSystem.TupleFactory> {

  private Sound sfxGameOver = Gdx.audio.newSound(Gdx.files.internal("sfx/GameOver.wav"));
  private Sound sfxShootBubble = Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (4).wav"));
  private Sound sfxJump = Gdx.audio.newSound(Gdx.files.internal("sfx/SFX (6).wav"));

  public PlayerSystem() {
    super(new PlayerSystem.TupleFactory());
  }


  public void execute(int tickTime, Tuple t, World world, GameContext context) {

    /* Check for player/monster collision. */
    CollisionComponent collisionComponent = t.entity.getComponent(CollisionComponent.class);
    if (collisionComponent.hasCollission()) {

      /* Check for monster collision. */
      var monsterHit = collisionComponent.getCollisions().anyMatch(entity -> entity.hasComponent(EnemyComponent.class));
      if (monsterHit) {
        sfxGameOver.play();
        context.setGameOver(tickTime);
        return;
      }
    }

    TransformComponent transformComponent = t.entity.getComponent(TransformComponent.class);
    InputComponent inputComponent = t.entity.getComponent(InputComponent.class);
    if (t.playerComponent.lastBubbleSpawnTime + PlayerComponent.BUBBLE_SHOOT_DELAY < tickTime && inputComponent.shoot) {
      t.playerComponent.lastBubbleSpawnTime = tickTime;
      var offsetX = t.playerComponent.movingDir == MoveDirection.Left ? -8 : +8;
      Point2f position = new Point2f(transformComponent.position.x + offsetX, transformComponent.position.y - 8);
      world.addEntity(EntityFactory.spawnBubble(tickTime, position, t.playerComponent.movingDir, StateBaseBubbleComponent.State.SHOOT));
      sfxShootBubble.play();
    }

    final float topSpeed = 3;
    final float g1 = t.playerComponent.gUp * -1;
    final float g2 = t.playerComponent.gDown * -1;
    final float maxImpulse = t.playerComponent.accelerationYUp * -1;
    final float minImpulse = t.playerComponent.accelerationYUp * -0.33f;

    float ya = 0;
//    if (t.playerComponent.jumpTickRemaining > 0) {
//      t.playerComponent.jumpTickRemaining -= 1;
//
//      var dt = (1.0f / PlayerComponent.JUMP_HEIGHT_IN_PIXEL) * (PlayerComponent.JUMP_HEIGHT_IN_PIXEL - t.playerComponent.jumpTickRemaining);
//
//      ya -= Math.max(Interpolation.linear.apply(dt) * 2, 0.2);
//      //ya -= 1;
//
//      /* Limit Jump */
//      if (!inputComponent.jump && t.playerComponent.jumpTickRemaining < PlayerComponent.JUMP_HEIGHT_IN_PIXEL_MIN_JUMP && t.playerComponent.jumpTickRemaining > PlayerComponent.JUMP_HEIGHT_IN_PIXEL_MIN_JUMP - 5) {
//        t.playerComponent.jumpTickRemaining = 0;
//      }
//
//    } else {
      boolean isOnGround = t.entity.getComponent(GroundDataComponent.class).isOnGround;
    if (isOnGround && t.playerComponent.velocityY >= 0) {
      if (inputComponent.jump) {
        t.playerComponent.jumpTickRemaining = PlayerComponent.JUMP_HEIGHT_IN_PIXEL;
        sfxJump.play();
        t.playerComponent.velocityY = maxImpulse; // Jump
        ya = t.playerComponent.velocityY;
        //t.playerComponent.velocityY += t.playerComponent.accelerationY;
      } else {
        t.playerComponent.velocityY = 0; // Jump
        ya = t.playerComponent.velocityY;
      }
      } else {
      //ya += 1;

      if (!inputComponent.jump && t.playerComponent.velocityY < minImpulse) {
        t.playerComponent.velocityY = minImpulse;
      }

      if (t.playerComponent.velocityY < 0) {
        t.playerComponent.velocityY += g1; // Gravity
      } else {
        t.playerComponent.velocityY += g2; // Gravity
      }
//        t.playerComponent.velocityY += g; // Gravity
      if (t.playerComponent.velocityY > maxImpulse * -1) {
        t.playerComponent.velocityY = maxImpulse * -1;
      }

      ya = t.playerComponent.velocityY;
//        t.playerComponent.velocityY += t.playerComponent.accelerationY;
    }
//    }

    final float maxX = t.playerComponent.maxVelocityX;
    final float accX = t.playerComponent.accelerationX;


    var moveX = 0.0f;
    var playerMoveDirection = t.playerComponent.movingDir;
    if (inputComponent.horizontal < 0) {
      t.playerComponent.velocityX -= accX;
      if (t.playerComponent.velocityX < maxX * -1) {
        t.playerComponent.velocityX = maxX * -1;
      }
//      moveX--;
      playerMoveDirection = MoveDirection.Left;
    } else if (inputComponent.horizontal > 0) {
      t.playerComponent.velocityX += accX;
      if (t.playerComponent.velocityX > maxX) {
        t.playerComponent.velocityX = maxX;
      }

      //      moveX++;
      playerMoveDirection = MoveDirection.Right;
    } else {
      if (t.playerComponent.velocityX > 0) {
        t.playerComponent.velocityX -= accX;
        if (t.playerComponent.velocityX < 0) {
          t.playerComponent.velocityX = 0.0f;
        }
      }
      if (t.playerComponent.velocityX < 0) {
        t.playerComponent.velocityX += accX;
        if (t.playerComponent.velocityX > 0) {
          t.playerComponent.velocityX = 0.0f;
        }
      }
    }
    moveX = t.playerComponent.velocityX;

    final Rect2i boundingBox = t.entity.getComponent(BoundingBoxComponent.class).getBoundingBox();
    moveX = LevelUtils.clipMoveX(moveX, transformComponent.position, boundingBox, world);

    /* Update player position. */
    Point2f position = t.entity.getComponent(TransformComponent.class).position;
    position.x += moveX * t.playerComponent.speed;
    position.y += LevelUtils.clipMoveY(ya * t.playerComponent.speed, transformComponent.position, boundingBox, world);

    LevelUtils.clipPositionToLevelBounds(transformComponent.position, boundingBox);


    t.playerComponent.movingDir = playerMoveDirection;

    AnimationComponent animationComponent = t.entity.getComponent(AnimationComponent.class);
    animationComponent.animation = Bubblun.ANIMATION_IDLE;
    animationComponent.currentAnimationStartTimeTick = 0;
    animationComponent.flipX = playerMoveDirection == MoveDirection.Left;

  }

  /**
   * Declares the component needed by this common.
   */
  static class Tuple implements ComponentSystem.Tuple {
    Entity entity;
    PlayerComponent playerComponent;

    Tuple(Entity entity, PlayerComponent playerComponent) {
      this.entity = entity;
      this.playerComponent = playerComponent;
    }
  }

  static class TupleFactory implements ComponentSystem.TupleFactory<Tuple> {

    public Tuple map(Entity entity) {
      var playerComponent = entity.getComponent(PlayerComponent.class);
      if (playerComponent != null) {
        return new Tuple(entity, playerComponent);
      }
      return null;
    }
  }
}