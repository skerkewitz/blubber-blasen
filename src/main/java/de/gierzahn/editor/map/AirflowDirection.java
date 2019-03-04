package de.gierzahn.editor.map;

public enum AirflowDirection {
  Empty("Empty", 0),
  UP("Up", 1),
  DOWN("Downe", 2),
  LEFT("Left", 3),
  RIGHT("Right", 4);

  private final String name;
  private final byte code;

  AirflowDirection(String name, int code) {
    this.name = name;
    this.code = (byte) code;
  }

  public static AirflowDirection fromCode(int at) {
    for (var af : values()) {
      if (af.code == at) {
        return af;
      }
    }

    throw new IllegalArgumentException("Unknown code " + at);
  }

  public byte getCode() {
    return code;
  }
}
