package de.gierzahn.editor.map.io;

import de.gierzahn.editor.map.EnemyBaseMapLayer;
import de.gierzahn.editor.map.Map;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultMapReader implements MapReader {

  @Override
  public Map read(InputStream inputStream) throws IOException {

    final Map map = new Map();
    final byte[] readBuffer = new byte[Map.NUM_TILES];
    map.staticMapLayer.fromByteArray(readBufferFully(inputStream, readBuffer));
    map.airflowMapLayer.fromByteArray(readBufferFully(inputStream, readBuffer));

    try { // be backward compatible
      /* Write the enemy layer. */
      DataInputStream dataInputStream = new DataInputStream(inputStream);
      int enemyCount = dataInputStream.readInt();
      for (int i = 0; i < enemyCount; i += 1) {
        int x = dataInputStream.readInt();
        int y = dataInputStream.readInt();
        EnemyBaseMapLayer.Enemy enemy = map.enemyMapLayer.addEnemyAt(new Point(x, y));

        enemy.type = dataInputStream.readInt();
        int isLookingLeft = dataInputStream.readInt();
        enemy.isLookingLeft = isLookingLeft == 1;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return map;
  }

  private byte[] readBufferFully(InputStream inputStream, byte[] readBuffer) throws IOException {
    IOUtils.readFully(inputStream, readBuffer);
    return readBuffer;
  }

}
