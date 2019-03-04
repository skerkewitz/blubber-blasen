package de.gierzahn.editor.map.io;

import de.gierzahn.editor.map.Map;

import java.io.IOException;
import java.io.InputStream;

public interface MapReader {

  Map read(InputStream inputStream) throws IOException;
}
