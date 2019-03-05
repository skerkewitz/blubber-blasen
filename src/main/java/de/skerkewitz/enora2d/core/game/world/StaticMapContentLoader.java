package de.skerkewitz.enora2d.core.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import de.gierzahn.editor.map.Map;
import de.gierzahn.editor.map.io.DefaultMapReader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class StaticMapContentLoader {

  private StaticMapContentLoader() {
    /* no instance allowed. */
  }

  public static StaticMapContent load(int levelNum) {
    try {
      return loadInternal(levelNum);
    } catch (IOException e) {
      throw new RuntimeException("Could not load level " + levelNum, e);
    }
  }

  public static class SpriteTileset {
    Sprite defaultBlock = null;

    Sprite fourBlock_topLeft;
    Sprite fourBlock_topright;
    Sprite fourBlock_bottomLeft;
    Sprite fourBlock_bottomRight;

    Sprite shadow_straightHorizontal;
    Sprite shadow_straightVertical;

    Sprite shadow_startHorizontal;
    Sprite shadow_startVertical;

    Sprite shadow_innerCorner;
    Sprite shadow_outerCorner;
  }

  private static StaticMapContent loadInternal(int levelNum) throws IOException {

    Gdx.app.log(StaticMapContentLoader.class.getSimpleName(), "Loading level " + levelNum);

    final String padded = StringUtils.leftPad("" + levelNum, 2, "0");
    FileHandle internal = Gdx.files.internal("level/round" + padded + ".map");

    DefaultMapReader defaultMapReader = new DefaultMapReader();
    Map map = defaultMapReader.read(internal.read());

    /* Also load the tile set for that level. */
    final Texture tilesetTexture = loadTilesetTexture(padded);

    SpriteTileset spriteTileset = new SpriteTileset();

    spriteTileset.defaultBlock = getTileSpriteFromTexture(tilesetTexture, 2, 0, 8, 8);

    spriteTileset.fourBlock_topLeft = getTileSpriteFromTexture(tilesetTexture, 0, 0, 8, 8);
    spriteTileset.fourBlock_topright = getTileSpriteFromTexture(tilesetTexture, 1, 0, 8, 8);
    spriteTileset.fourBlock_bottomLeft = getTileSpriteFromTexture(tilesetTexture, 0, 1, 8, 8);
    spriteTileset.fourBlock_bottomRight = getTileSpriteFromTexture(tilesetTexture, 1, 1, 8, 8);

    spriteTileset.shadow_straightHorizontal = getTileSpriteFromTexture(tilesetTexture, 3, 1, 8, 8);
    spriteTileset.shadow_straightVertical = getTileSpriteFromTexture(tilesetTexture, 2, 2, 8, 8);

    spriteTileset.shadow_startHorizontal = getTileSpriteFromTexture(tilesetTexture, 5, 1, 8, 8);
    spriteTileset.shadow_startVertical = getTileSpriteFromTexture(tilesetTexture, 4, 0, 8, 8);

    spriteTileset.shadow_innerCorner = getTileSpriteFromTexture(tilesetTexture, 2, 1, 8, 8);
    spriteTileset.shadow_outerCorner = getTileSpriteFromTexture(tilesetTexture, 4, 1, 8, 8);

    return new StaticMapContent(map, spriteTileset);
  }

  private static Sprite getTileSpriteFromTexture(Texture tilesetTexture, int x, int y, int w, int h) {
    final Sprite sprite = new Sprite(tilesetTexture);
    sprite.setSize(w, h);
    sprite.setRegion(x * 8, y * 8, 8, 8);
    sprite.setFlip(false, true);
    return sprite;
  }

  private static Texture loadTilesetTexture(String padded) {
    FileHandle tilesetFileHandle = Gdx.files.internal("level/round" + padded + ".png");
    if (!tilesetFileHandle.exists()) {
      tilesetFileHandle = Gdx.files.internal("level/round01.png");
    }
    return new Texture(tilesetFileHandle);
  }
}
