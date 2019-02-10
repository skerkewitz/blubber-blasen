package de.skerkewitz.blubberblase.entity;

import de.skerkewitz.blubberblase.Ressources;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.common.Rect2i;
import de.skerkewitz.enora2d.core.ecs.component.SpriteComponent;
import de.skerkewitz.enora2d.core.ecs.component.Transform;
import de.skerkewitz.enora2d.core.ecs.entity.Entity;
import de.skerkewitz.enora2d.core.gfx.RenderSprite;
import de.skerkewitz.enora2d.core.gfx.RgbColorPalette;
import de.skerkewitz.enora2d.core.input.InputHandler;

public class EntityFactory {

  public static Entity spawnBubblun(InputHandler inputHandler) {

//    var sheet = new SpriteSheet(new ImageData("/sprite_sheet.png"));

    Bubblun bubblun = new Bubblun(inputHandler);
    bubblun.addComponent(new Transform(new Point2i(4 * 8, 25 * 8)));
    bubblun.addComponent(new SpriteComponent());
//    SpriteComponent spriteComponent = bubblun.getComponent(SpriteComponent.class);
//    spriteComponent.colorPalette = RgbColorPalette.mergeColorCodes(RgbColorPalette.NONE, RgbColorPalette.GREEN, RgbColorPalette.NONE, RgbColorPalette.WHITE);
//    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25* 8, 16, 16), new ImageData("/sprite_sheet.png"));
    return bubblun;
  }

  public static Entity spawnBubble(int posX, int posY, int speed) {
    Bubble bubble = new Bubble(speed);
    bubble.addComponent(new Transform(new Point2i(posX, posY)));
    bubble.addComponent(new SpriteComponent());

    SpriteComponent spriteComponent = bubble.getComponent(SpriteComponent.class);
    spriteComponent.colorPalette = RgbColorPalette.mergeColorCodes(RgbColorPalette.NONE, RgbColorPalette.GREEN, RgbColorPalette.NONE, RgbColorPalette.WHITE);
    spriteComponent.renderSprite = new RenderSprite(new Rect2i(0, 25 * 8, 16, 16), Ressources.SpriteSheet);
    return bubble;
  }

  public static Entity spawnZenChan() {
//    var spritesheet = new SpriteSheet(new ImageData("/Enemies.png"));
//    var sprite = new Screen.Sprite(1, spritesheet);

    ZenChan zenChan = new ZenChan(1);

    zenChan.addComponent(new Transform(new Point2i(8 * 8, 24 * 8)));
    zenChan.addComponent(new SpriteComponent());

    return zenChan;
  }
}
