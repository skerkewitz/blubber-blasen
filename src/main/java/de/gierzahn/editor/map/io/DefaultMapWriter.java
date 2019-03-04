package de.gierzahn.editor.map.io;

import de.gierzahn.editor.map.Map;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;

public class DefaultMapWriter implements MapWriter {

  @Override
  public void write(Map map, OutputStream outputStream) throws IOException {
    IOUtils.write(map.staticMapLayer.toByteArray(), outputStream);
    IOUtils.write(map.airflowMapLayer.toByteArray(), outputStream);
  }

}
