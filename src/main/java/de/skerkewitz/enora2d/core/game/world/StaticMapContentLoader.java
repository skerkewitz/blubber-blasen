package de.skerkewitz.enora2d.core.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.skerkewitz.enora2d.core.game.world.tiles.TileContainer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

  private static StaticMapContent loadInternal(int levelNum) throws IOException {

    Gdx.app.log(StaticMapContentLoader.class.getSimpleName(), "Loading level " + levelNum);

    final String padded = StringUtils.leftPad("" + levelNum, 2, "0");
    FileHandle internal = Gdx.files.internal("level/level" + padded + ".txt");
    List<String> strings = IOUtils.readLines(internal.read(), StandardCharsets.UTF_8);

    /* Ignore topline. */
    strings = strings.subList(1, strings.size());

    var tileWidth = StaticMapContent.WIDTH / 8;
    var tileHeight = StaticMapContent.HEIGHT / 8;
    var tiles = new byte[tileWidth * tileHeight];

    for (int y = 0; y < tileHeight; y++) {
      var l = strings.get(y);
      for (int x = 0; x < tileWidth; x++) {
        int index = x + y * tileWidth;
        switch (l.charAt(x)) {
          case '#':
            tiles[index] = TileContainer.BB_STONE.getId();
            break;
          default:
            tiles[index] = TileContainer.VOID.getId();
            break;
        }
      }
    }

    return new StaticMapContent(tiles);
  }
}
