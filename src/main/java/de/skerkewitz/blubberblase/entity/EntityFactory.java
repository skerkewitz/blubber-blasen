package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.component.*;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.entity.DefaultEntity;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.input.InputHandler;

import java.util.EnumSet;

public class EntityFactory {


  public static Entity spawnBubblun(InputHandler inputHandler) {

//    var sheet = new SpriteSheet(new ImageData("/sprite_sheet.png"));

    Bubblun entity = new Bubblun();
    entity.addComponent(new TransformComponent(new Point2i(4 * 8, 25 * 8)));
    entity.addComponent(new InputComponent(inputHandler));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new GroundDataComponent(-4, 4, 0));
    entity.addComponent(new AnimationComponent(0, Bubblun.ANIMATION_IDLE, false));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -16, 16, 16)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.PLAYER), EnumSet.of(CollisionComponent.Layer.BUBBLE, CollisionComponent.Layer.ENEMY)));
    AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
//    animationComponent.animation = ANIMATION_IDLE;
//    animationComponent.currentAnimationStartTimeTick = 0;


    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubblun.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -16);
//    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25* 8, 16, 16), new ImageData("/sprite_sheet.png"));
    return entity;
  }

  public static Entity spawnBubble(int tickTime, Point2i position, MoveableLegacyEntity.MoveDirection moveDirection, AiBubbleComponent.State state) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new AiBubbleComponent(tickTime, state));
    entity.addComponent(new LifeTimeComponent(tickTime, Bubble.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new MovementComponent(tickTime, moveDirection, state == AiBubbleComponent.State.SHOOT ? 4 : 1));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -8, 12, 12)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.BUBBLE), EnumSet.of(CollisionComponent.Layer.PLAYER, CollisionComponent.Layer.ENEMY)));

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubble.COLOR_PALETTE;
    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25 * 8, 16, 16), Ressources.SpriteSheet);
    spriteComponent.pivotPoint = new Point2i(-8, -8);
    return entity;
  }

  public static Entity spawnCaptureBubble(int tickTime, Point2i position) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new AiBubbleComponent(tickTime, AiBubbleComponent.State.FLOAT));
    entity.addComponent(new LifeTimeComponent(tickTime, Bubble.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new MovementComponent(tickTime, MoveableLegacyEntity.MoveDirection.Up, 1));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -8, 12, 12)));
//    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.BUBBLE), EnumSet.of(CollisionComponent.Layer.PLAYER, CollisionComponent.Layer.ENEMY)));

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = RgbColorPalette.mergeColorCodes(RgbColorPalette.NONE, RgbColorPalette.BLACK, 533, RgbColorPalette.GREEN);
    spriteComponent.renderSprite = new RenderSprite(new Rect2i(131, 5, 16, 16), Ressources.SpriteSheet_Enemies);
    spriteComponent.pivotPoint = new Point2i(-8, -8);
    return entity;
  }

  public static Entity spawnZenChan(Point2i position) {
//    var spritesheet = new SpriteSheet(new ImageData("/Enemies.png"));
//    var sprite = new Screen.Sprite(1, spritesheet);

    ZenChan entity = new ZenChan(1);

    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new AnimationComponent(0, ZenChan.ANIMATION_IDLE, false));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -16, 16, 16)));
    entity.addComponent(new CollisionComponent(EnumSet.of(CollisionComponent.Layer.ENEMY), EnumSet.of(CollisionComponent.Layer.PLAYER, CollisionComponent.Layer.BUBBLE)));
    entity.addComponent(new GroundDataComponent(-4, 4, 0));

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = ZenChan.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -16);

    return entity;
  }

  private static Entity newEntity() {
    return new DefaultEntity();
  }
}
