package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.blubberblase.esc.component.*;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.entity.DefaultEntity;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.entity.MoveableLegacyEntity;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.input.InputHandler;

public class EntityFactory {


  public static Entity spawnBubblun(InputHandler inputHandler) {

//    var sheet = new SpriteSheet(new ImageData("/sprite_sheet.png"));

    Bubblun entity = new Bubblun();
    entity.addComponent(new TransformComponent(new Point2i(4 * 8, 25 * 8)));
    entity.addComponent(new InputComponent(inputHandler));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new GroundDataComponent(0, 0, 0));
    entity.addComponent(new AnimationComponent(0, Bubblun.ANIMATION_IDLE, false));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -16, 16, 16)));
    entity.addComponent(new CollisionComponent());
    AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
//    animationComponent.animation = ANIMATION_IDLE;
//    animationComponent.currentAnimationStartTimeTick = 0;


    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubblun.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -15);
//    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25* 8, 16, 16), new ImageData("/sprite_sheet.png"));
    return entity;
  }

  public static Entity spawnBubble(int tickTime, Point2i position, MoveableLegacyEntity.MoveDirection moveDirection) {
    Entity entity = newEntity();
    entity.addComponent(new TransformComponent(position));
    entity.addComponent(new SpriteComponent());
    entity.addComponent(new AiComponent());
    entity.addComponent(new LifeTimeComponent(tickTime, Bubble.MAX_LIFETIME_IN_TICKS));
    entity.addComponent(new MovementComponent(tickTime, moveDirection));
    entity.addComponent(new BoundingBoxComponent(new Rect2i(-8, -8, 16, 16)));
    entity.addComponent(new CollisionComponent());

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubble.COLOR_PALETTE;
    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25 * 8, 16, 16), Ressources.SpriteSheet);
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
    entity.addComponent(new BoundingBoxComponent(new Rect2i(0, 0, 16, 16)));
    entity.addComponent(new CollisionComponent());

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = ZenChan.COLOR_PALETTE;

    return entity;
  }

  private static Entity newEntity() {
    return new DefaultEntity();
  }
}
