package de.skerkewitz.blubberblase.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.*;
import de.skerkewitz.blubberblase.util.TimeUtil;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.DefaultEntity;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.ecs.common.LifeTimeComponent;
import de.skerkewitz.enora2d.core.ecs.common.SoundComponent;
import de.skerkewitz.enora2d.core.ecs.common.TransformAnimatorComponent;
import de.skerkewitz.enora2d.core.ecs.common.TransformComponent;
import de.skerkewitz.enora2d.core.gfx.Animation;
import de.skerkewitz.enora2d.core.gfx.SpriteSource;
import de.skerkewitz.enora2d.core.input.InputHandler;

import java.util.EnumSet;

public class EntityFactory {

  private final static byte SPRITE_PRIORITY_BUBBLE = 0;
  private final static byte SPRITE_PRIORITY_BONUS_ITEMS = 2;
  private final static byte SPRITE_PRIORITY_TRAP_BUBBLE = 3;

  private final static byte SPRITE_PRIORITY_BONUS_POINT_PICKUP_ITEMS = 50;

  private final static byte SPRITE_PRIORITY_ENEMY = 100;
  private final static byte SPRITE_PRIORITY_PLAYER = 120;


  public static Entity spawnBubblun(InputHandler inputHandler, Point2f position) {

    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new PlayerComponent());
    entity.addComponent(new InputComponent(inputHandler));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.colorPalette = Bubblun.COLOR_PALETTE;
      component.pivotPoint = new Point2i(-8, -15);
      component.setPriority(SPRITE_PRIORITY_PLAYER);
    });
    entity.addComponent(new GroundDataComponent(-4, 4, 0));
    entity.addComponent(new AnimationComponent(0, Bubblun.ANIMATION_IDLE, false, 0));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -12, 12, 13)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.PLAYER), EnumSet.of(CollisionComponent.Layer.BUBBLE, CollisionComponent.Layer.TRAP_BUBBLE, CollisionComponent.Layer.ENEMY, CollisionComponent.Layer.BONUS)));

    return entity;
  }

  public static Entity spawnBubble(int tickTime, Point2f position, MoveDirection moveDirection, StateBaseBubbleComponent.State state) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.spriteSource = new SpriteSource(new Rect2i(0, 25 * 8, 16, 16), Ressources.SpriteSheet_Bubble);
      component.pivotPoint = new Point2i(-8, -8);
      component.setPriority(SPRITE_PRIORITY_BUBBLE);
    });
    entity.addComponent(new StateBaseBubbleComponent(tickTime, state, StateBaseBubbleComponent.Type.NORMAL, false));
    entity.addComponent(new LifeTimeComponent(tickTime, Bubble.MAX_LIFETIME_BEFORE_BURST), component -> component.autoRemove = false);
    entity.addComponent(new MovementComponent(tickTime, moveDirection, state == StateBaseBubbleComponent.State.SHOOT ? 4 : 1));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -6, 12, 12)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.BUBBLE), EnumSet.of(CollisionComponent.Layer.PLAYER, CollisionComponent.Layer.ENEMY)));
    entity.addComponent(new AnimationComponent(0, Bubble.BUBBLE, false, Bubble.FRAME_ANIMATION_SPEED_RND_OFFSET), component -> {
      component.animation.setLoopStyle(Animation.LoopStyle.CycleForwardBackward);
    });

    return entity;
  }

  public static Entity spawnBubbleBurst(int tickTime, Point2f position) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.spriteSource = new SpriteSource(new Point2i(0, 0), Ressources.SpriteSheet_BubbleBurst);
      component.pivotPoint = new Point2i(-8, -8);
      component.setPriority(SPRITE_PRIORITY_BUBBLE);
    });
    entity.addComponent(new LifeTimeComponent(tickTime, Bubble.BURST_MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new RenderSpriteAlphaAnimatorComponent(1.0f, 0.0f, tickTime, tickTime + Bubble.BURST_MAX_LIFETIME_IN_TICKS, Interpolation.circle));
    entity.addComponent(new SoundComponent(Bubble.sfxBurstBubble, 0.5f));

    return entity;
  }

  public static Entity spawnTrapBubble(int tickTime, Point2f position) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.pivotPoint = new Point2i(-8, -8);
      component.setPriority(SPRITE_PRIORITY_TRAP_BUBBLE);
    });
    entity.addComponent(new StateBaseBubbleComponent(tickTime, StateBaseBubbleComponent.State.FLOAT, StateBaseBubbleComponent.Type.TRAP, false));
    entity.addComponent(new LifeTimeComponent(tickTime, TrapBubble.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new MovementComponent(tickTime, MoveDirection.Up, Bubble.TRAPPED_SPEED));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-4, -4, 8, 8)));
    entity.addComponent(new AnimationComponent(0, TrapBubble.ANIMATION_IDLE, false, TrapBubble.FRAME_ANIMATION_SPEED_RND_OFFSET), component -> {
      component.animation.setLoopStyle(Animation.LoopStyle.CycleForwardBackward);
    });
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.TRAP_BUBBLE), EnumSet.of(CollisionComponent.Layer.PLAYER)));

    return entity;
  }

  public static Entity spawnDiamond(int tickTime, Point2f position) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.spriteSource = new SpriteSource(new Rect2i(0, 2 * 8, 16, 16), Ressources.SpriteSheet);
      component.colorPalette = BonusDiamond.COLOR_PALETTE;
      component.pivotPoint = new Point2i(-8, -15);
      component.setPriority(SPRITE_PRIORITY_BONUS_ITEMS);
    });
    entity.addComponent(new BonusItemComponent());
    entity.addComponent(new LifeTimeComponent(tickTime, BonusDiamond.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -10, 13, 11)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.BONUS), EnumSet.of(CollisionComponent.Layer.PLAYER)));

    return entity;
  }

  public static Entity spawnThrownEnemy(int frameCount, Point2f position, MoveDirection currentMoveDirection) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.colorPalette = ZenChan.THROW_COLOR_PALETTE;
      component.pivotPoint = new Point2i(-8, -15);
      component.setPriority(SPRITE_PRIORITY_BONUS_ITEMS);
    });
    entity.addComponent(new LifeTimeComponent(frameCount, ThrownEnemy.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -8, 12, 12)));
    entity.addComponent(new AnimationComponent(0, ZenChan.THROW, false, 0));
    entity.addComponent(new ThrownEnemyComponent(frameCount, new Point2i(currentMoveDirection.getHorizontalMoveVector(), -1)));
    entity.addComponent(new SoundComponent(Bubble.sfxBurstBubble, 1.0f));

    return entity;
  }

  public static Entity spawnZenChan(Point2f position, int tickTime, MoveDirection movingDir, boolean isAngry) {

    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new EnemyComponent(isAngry));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.colorPalette = ZenChan.COLOR_PALETTE;
      component.pivotPoint = ZenChan.spritePivotPoint;
      component.setPriority(SPRITE_PRIORITY_ENEMY);
    });
    entity.addComponent(new AnimationComponent(0, ZenChan.ANIMATION_IDLE, movingDir == MoveDirection.Right, 0));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -14, 12, 15)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.ENEMY), EnumSet.of(CollisionComponent.Layer.PLAYER, CollisionComponent.Layer.BUBBLE)));
    entity.addComponent(new GroundDataComponent(-4, 4, 0));
    entity.addComponent(new MovementComponent(tickTime, movingDir, 1));

    return entity;
  }

  public static Entity spawnTextEntity(Point2f position, RenderTextComponent.Text text, Color color) {

    final Entity entity = EntityFactory.newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new RenderTextComponent(text, null), component -> {
      component.spriteSource = new SpriteSource(new Point2i(0, 0), Ressources.SpriteSheet_Text);
      component.color = color;
    });

    return entity;
  }

  public static Entity spawnPointPickup(int tickTime, Point2f position) {

    int lifetime = TimeUtil.secondsToTickTime(1);

    final Entity entity = EntityFactory.newEntity();
    entity.addComponent(new LifeTimeComponent(tickTime, lifetime));
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new RenderSpriteComponent(), component -> {
      component.size = RenderSpriteComponent.Size.Wide;
      component.spriteSource = new SpriteSource(new Point2i(0, 0), Ressources.SpriteSheet_PickupPoints);
      component.pivotPoint = new Point2i(-8, -8);
      component.setPriority(SPRITE_PRIORITY_BONUS_POINT_PICKUP_ITEMS);
    });

    entity.addComponent(new TransformAnimatorComponent(position, position.plus(0.0f, -25.0f), tickTime, tickTime + lifetime, Interpolation.linear));
    entity.addComponent(new RenderSpriteAlphaAnimatorComponent(1.0f, 0.0f, tickTime, tickTime + lifetime, Interpolation.pow5In));

    return entity;
  }


  public static Entity newEntity() {
    return new DefaultEntity();
  }


}
