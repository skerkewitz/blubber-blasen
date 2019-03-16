package de.skerkewitz.blubberblase.esc;

import com.badlogic.gdx.graphics.Color;
import de.skerkewitz.enora2d.common.Point2i;
import de.skerkewitz.enora2d.core.ecs.AbstractRenderComponent;
import de.skerkewitz.enora2d.core.gfx.SpriteSource;

public class RenderTextComponent extends AbstractRenderComponent {

  /**
   * How much we need to move the upper left corner of the sprite relative to the transform position.
   */
  public Point2i pivotPoint = Point2i.ZERO;
  /**
   * The actual sprite to render.
   */
  public SpriteSource spriteSource = null;

  public String text;
  public Color color = Color.WHITE;


  public RenderTextComponent(String text, SpriteSource spriteSource) {
    this.text = text;
    this.spriteSource = spriteSource;
  }
}
