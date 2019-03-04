package de.gierzahn.editor.map.io;

import de.gierzahn.editor.map.Map;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class DefaultMapReader implements MapReader {

  @Override
  public Map read(InputStream inputStream) throws IOException {

    final Map map = new Map();
    final byte[] readBuffer = new byte[Map.NUM_TILES];
    map.staticMapLayer.fromByteArray(readBufferFully(inputStream, readBuffer));
    map.airflowMapLayer.fromByteArray(readBufferFully(inputStream, readBuffer));

    return map;
  }

  private byte[] readBufferFully(InputStream inputStream, byte[] readBuffer) throws IOException {
    IOUtils.readFully(inputStream, readBuffer);
    return readBuffer;
  }

}
