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

    Bubblun bubblun = new Bubblun(inputHandler);
    bubblun.addComponent(new TransformComponent(new Point2i(4 * 8, 25 * 8)));
    bubblun.addComponent(new SpriteComponent());
    bubblun.addComponent(new GroundDataComponent(0, 0, 0));
    bubblun.addComponent(new AnimationComponent(0, Bubblun.ANIMATION_IDLE, false));
    bubblun.addComponent(new BoundingBoxComponent(new Rect2i(0, 0, 16, 16)));
    AnimationComponent animationComponent = bubblun.getComponent(AnimationComponent.class);
//    animationComponent.animation = ANIMATION_IDLE;
//    animationComponent.currentAnimationStartTimeTick = 0;


    SpriteComponent spriteComponent = bubblun.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubblun.COLOR_PALETTE;
    spriteComponent.pivotPoint = new Point2i(-8, -15);
//    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25* 8, 16, 16), new ImageData("/sprite_sheet.png"));
    return bubblun;
  }

  public static Entity spawnBubble(int tickTime, Point2i position) {
    Entity bubble = newEntity();
    bubble.addComponent(new TransformComponent(position));
    bubble.addComponent(new SpriteComponent());
    bubble.addComponent(new AiComponent());
    bubble.addComponent(new LifeTimeComponent(tickTime, Bubble.MAX_LIFETIME_IN_TICKS));
    bubble.addComponent(new MovementComponent(tickTime, MoveableLegacyEntity.MoveDirection.Right));

    SpriteComponent spriteComponent = bubble.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = Bubble.COLOR_PALETTE;
    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25 * 8, 16, 16), Ressources.SpriteSheet);
    return bubble;


  }

  public static Entity spawnZenChan() {
//    var spritesheet = new SpriteSheet(new ImageData("/Enemies.png"));
//    var sprite = new Screen.Sprite(1, spritesheet);

    ZenChan zenChan = new ZenChan(1);

    zenChan.addComponent(new TransformComponent(new Point2i(8 * 8, 24 * 8)));
    zenChan.addComponent(new SpriteComponent());
    zenChan.addComponent(new AnimationComponent(0, ZenChan.ANIMATION_IDLE, false));
    zenChan.addComponent(new BoundingBoxComponent(new Rect2i(0, 0, 15, 15)));

    SpriteComponent spriteComponent = zenChan.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = ZenChan.COLOR_PALETTE;

    return zenChan;
  }

  private static Entity newEntity() {
    return new DefaultEntity();
  }
}
