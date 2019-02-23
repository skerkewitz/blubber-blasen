package de.skerkewitz.enora2d.core.game.level.tiles;

import de.skerkewitz.enora2d.core.game.level.BackgroundLayer;
import de.skerkewitz.enora2d.core.gfx.Screen;

public class BasicTile extends Tile {

  protected int tileId;
  protected int tileColour;

  public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
    super(id, false, false, levelColour);
    this.tileId = x + y * 32;
    this.tileColour = tileColour;
  }

  public int getTileColour() {
    return tileColour;
  }

  public void tick() {
  }

  public void render(Screen screen, BackgroundLayer backgroundLayer, int x, int y) {
    screen.render(x, y, tileId, tileColour, 0x00, 1);
  }

  public int getTileId() {
    return tileId;
  }
}
