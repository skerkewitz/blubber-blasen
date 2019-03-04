package de.gierzahn.editor.map.io;

import de.gierzahn.editor.map.Map;

import java.io.IOException;
import java.io.OutputStream;

public interface MapWriter {

  void write(Map map, OutputStream outputStream) throws IOException;
}
