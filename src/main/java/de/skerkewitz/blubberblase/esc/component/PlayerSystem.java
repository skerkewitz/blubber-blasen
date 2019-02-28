package de.skerkewitz.blubberblase.esc.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.skerkewitz.blubberblase.GameContext;
import de.skerkewitz.blubberblase.entity.Bubblun;
import de.skerkewitz.blubberblase.entity.EntityFactory;
import de.skerkewitz.blubberblase.entity.LevelUtils;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.BaseComponentSystem;
import de.skerkewitz.enora2d.core.ecs.ComponentSystem;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.game.world.World;

/**
 * A system to render all SpriteComponents.
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
        context.gameOver = true;
        return;
      }
    }

    TransformComponent transformComponent = t.entity.getComponent(TransformComponent.class);
    InputComponent inputComponent = t.entity.getComponent(InputComponent.class);
    if (t.playerComponent.lastBubbleSpawnTime + PlayerComponent.BUBBLE_SHOOT_DELAY < tickTime && inputComponent.shoot) {
      t.playerComponent.lastBubbleSpawnTime = tickTime;
      var offsetX = t.playerComponent.movingDir == MoveDirection.Left ? -8 : +8;
      Point2f position = new Point2f(transformComponent.position.x + offsetX, transformComponent.position.y - 8);
      world.addEntity(EntityFactory.spawnBubble(tickTime, position, t.playerComponent.movingDir, AiBubbleComponent.State.SHOOT));
      sfxShootBubble.play();
    }


    int ya = 0;
    if (t.playerComponent.jumpTickRemaining > 0) {
      t.playerComponent.jumpTickRemaining -= 1;
      ya -= 1;
    } else {
      boolean isOnGround = t.entity.getComponent(GroundDataComponent.class).isOnGround;
      if (isOnGround) {
        if (inputComponent.jump && isOnGround) {
          t.playerComponent.jumpTickRemaining = PlayerComponent.JUMP_HEIGHT_IN_PIXEL;
          sfxJump.play();
        }
      } else {
        ya += 1;
      }
    }

    var moveX = 0;
    var playerMoveDirection = t.playerComponent.movingDir;
    if (inputComponent.horizontal < 0) {
      moveX--;
      playerMoveDirection = MoveDirection.Left;
    } else if (inputComponent.horizontal > 0) {
      moveX++;
      playerMoveDirection = MoveDirection.Right;
    }

    moveX = LevelUtils.clipMoveX(moveX, transformComponent.position, t.entity.getComponent(BoundingBoxComponent.class).getBoundingBox(), world);

    /* Update player position. */
    Point2f position = t.entity.getComponent(TransformComponent.class).position;
    position.x += moveX * t.playerComponent.speed;
    position.y += ya * t.playerComponent.speed;


    t.playerComponent.movingDir = playerMoveDirection;

    AnimationComponent animationComponent = t.entity.getComponent(AnimationComponent.class);
    animationComponent.animation = Bubblun.ANIMATION_IDLE;
    animationComponent.currentAnimationStartTimeTick = 0;
    animationComponent.flipX = playerMoveDirection == MoveDirection.Left;

  }

  /**
   * Declares the component needed by this system.
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