package de.skerkewitz.enora2d.core.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

  private static StaticMapContent loadInternal(int levelNum) throws IOException {

    Gdx.app.log(StaticMapContentLoader.class.getSimpleName(), "Loading level " + levelNum);

    final String padded = StringUtils.leftPad("" + levelNum, 2, "0");
    FileHandle internal = Gdx.files.internal("level/round" + padded + ".map");

    DefaultMapReader defaultMapReader = new DefaultMapReader();
    Map map = defaultMapReader.read(internal.read());

    return new StaticMapContent(map);
  }
}
