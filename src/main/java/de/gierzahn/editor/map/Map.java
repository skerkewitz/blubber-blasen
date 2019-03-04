package de.gierzahn.editor.map;

public class Map {

  public enum Layer {
    Static,
    Airflow
  }

  // 32x32 tile
  public static final int NUM_TILES_HORIZONTAL = 32;
  public static final int NUM_TILES_VERTICAL = 26;

  public static final int TILE_WIDTH = 8;
  public static final int TILE_HEIGHT = 8;

  public static final int MAX_LEFT = 0;
  public static final int MAX_RIGHT = NUM_TILES_HORIZONTAL - 1;
  public static final int MAX_UP = 0;
  public static final int MAX_DOWN = NUM_TILES_VERTICAL - 1;

  public static final int NUM_TILES = NUM_TILES_HORIZONTAL * NUM_TILES_VERTICAL;

  public static final int PANEL_WIDTH = NUM_TILES_HORIZONTAL * TILE_WIDTH;
  public static final int PANEL_HEIGHT = NUM_TILES_VERTICAL * TILE_HEIGHT;

  public final BaseMapLayer staticMapLayer = new BaseMapLayer(NUM_TILES_HORIZONTAL, NUM_TILES_VERTICAL);
  public final BaseMapLayer airflowMapLayer = new BaseMapLayer(NUM_TILES_HORIZONTAL, NUM_TILES_VERTICAL);

}
