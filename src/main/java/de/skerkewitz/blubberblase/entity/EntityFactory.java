package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.*;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.DefaultEntity;
import de.skerkewitz.enora2d.core.ecs.Entity;
import de.skerkewitz.enora2d.core.ecs.MoveDirection;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.input.InputHandler;

import java.util.EnumSet;

public class EntityFactory {

  private final static byte SPRITE_PRIORITY_BUBBLE = 0;
  private final static byte SPRITE_PRIORITY_BONUS_ITEMS = 2;
  private final static byte SPRITE_PRIORITY_TRAP_BUBBLE = 3;
  private final static byte SPRITE_PRIORITY_ENEMY = 4;
  private final static byte SPRITE_PRIORITY_PLAYER = 5;


  public static Entity spawnBubblun(InputHandler inputHandler, Point2f position) {

//    var sheet = new SpriteSheet(new ImageData("/sprite_sheet.png"));

    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new PlayerComponent());
    entity.addComponent(new InputComponent(inputHandler));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new GroundDataComponent(-4, 4, 0));
    entity.addComponent(new AnimationComponent(0, Bubblun.ANIMATION_IDLE, false));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -12, 12, 13)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.PLAYER), EnumSet.of(CollisionComponent.Layer.BUBBLE, CollisionComponent.Layer.TRAP_BUBBLE, CollisionComponent.Layer.ENEMY, CollisionComponent.Layer.BONUS)));
    AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
//    animationComponent.animation = ANIMATION_IDLE;
//    animationComponent.currentAnimationStartTimeTick = 0;


    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubblun.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -15);
    spriteComponent.priority = SPRITE_PRIORITY_PLAYER;
//    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25* 8, 16, 16), new ImageData("/sprite_sheet.png"));
    return entity;
  }

  public static Entity spawnBubble(int tickTime, Point2f position, MoveDirection moveDirection, StateBaseBubbleComponent.State state) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new StateBaseBubbleComponent(tickTime, state, StateBaseBubbleComponent.Type.NORMAL, false));
    entity.addComponent(new LifeTimeComponent(tickTime, Bubble.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new MovementComponent(tickTime, moveDirection, state == StateBaseBubbleComponent.State.SHOOT ? 4 : 1));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -6, 12, 12)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.BUBBLE), EnumSet.of(CollisionComponent.Layer.PLAYER, CollisionComponent.Layer.ENEMY)));

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubble.COLOR_PALETTE;
    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25 * 8, 16, 16), Ressources.SpriteSheet);
    spriteComponent.pivotPoint = new Point2i(-8, -8);
    spriteComponent.priority = SPRITE_PRIORITY_BUBBLE;
    return entity;
  }

  public static Entity spawnTrapBubble(int tickTime, Point2f position) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new StateBaseBubbleComponent(tickTime, StateBaseBubbleComponent.State.FLOAT, StateBaseBubbleComponent.Type.TRAP, false));
    entity.addComponent(new LifeTimeComponent(tickTime, TrapBubble.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new MovementComponent(tickTime, MoveDirection.Up, Bubble.TRAPPED_SPEED));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-4, -4, 8, 8)));
    entity.addComponent(new AnimationComponent(0, TrapBubble.IDLE, false));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.TRAP_BUBBLE), EnumSet.of(CollisionComponent.Layer.PLAYER)));

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = TrapBubble.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -8);
    spriteComponent.priority = SPRITE_PRIORITY_TRAP_BUBBLE;
    return entity;
  }

  public static Entity spawnDiamond(int tickTime, Point2f position) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new BonusItemComponent());
    entity.addComponent(new LifeTimeComponent(tickTime, BonusDiamond.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -10, 13, 11)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.BONUS), EnumSet.of(CollisionComponent.Layer.PLAYER)));


    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 2 * 8, 16, 16), Ressources.SpriteSheet);
    spriteComponent.colorPalette = BonusDiamond.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -15);
    spriteComponent.priority = SPRITE_PRIORITY_BONUS_ITEMS;
    return entity;
  }

  public static Entity spawnThrownEnemy(int frameCount, Point2f position, MoveDirection currentMoveDirection) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new LifeTimeComponent(frameCount, ThrownEnemy.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -8, 12, 12)));
    entity.addComponent(new AnimationComponent(0, ZenChan.THROW, false));
    entity.addComponent(new ThrownEnemyComponent(frameCount, new Point2i(currentMoveDirection.getHorizontalMoveVector(), -1)));
    //    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.TRAP_BUBBLE), EnumSet.of(CollisionComponent.Layer.PLAYER)));

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = ZenChan.THROW_COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -15);
    spriteComponent.priority = SPRITE_PRIORITY_BONUS_ITEMS;
    return entity;
  }

  public static Entity spawnZenChan(Point2f position, int tickTime, MoveDirection movingDir, boolean isAngry) {

    Entity entity = newEntity();

    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new EnemyComponent(isAngry));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new AnimationComponent(0, ZenChan.ANIMATION_IDLE, movingDir == MoveDirection.Right));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-6, -14, 12, 15)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.ENEMY), EnumSet.of(CollisionComponent.Layer.PLAYER, CollisionComponent.Layer.BUBBLE)));
    entity.addComponent(new GroundDataComponent(-4, 4, 0));

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = ZenChan.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -15);
    spriteComponent.priority = SPRITE_PRIORITY_ENEMY;

    entity.addComponent(new MovementComponent(tickTime, movingDir, 1));

    return entity;
  }

  private static Entity newEntity() {
    return new DefaultEntity();
  }
}
