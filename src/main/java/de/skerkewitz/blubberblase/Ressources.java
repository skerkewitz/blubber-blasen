package de.skerkewitz.blubberblase;

import de.skerkewitz.enora2d.common.Size2i;
import de.skerkewitz.enora2d.core.gfx.NamedResource;
import de.skerkewitz.enora2d.core.gfx.SpriteSheet;

public interface Ressources {
  Size2i Size5x5 = new Size2i(5, 5);
  Size2i Size16x16 = new Size2i(16, 16);

  NamedResource SpriteSheet = new NamedResource("/sprite_sheet.png", new SpriteSheet(Size2i.ZERO, Size2i.ZERO, Size16x16));
  NamedResource SpriteSheet_Enemies = new NamedResource("/Enemies.png", new SpriteSheet(Size5x5, Size5x5, Size16x16));
}
