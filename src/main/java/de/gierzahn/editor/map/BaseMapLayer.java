package de.gierzahn.editor.map;

public class BaseMapLayer {

  private int width;
  private int height;

  private int[] data;

  public BaseMapLayer(int width, int height) {
    this.width = width;
    this.height = height;
    this.data = new int[width * height];
  }

  private int indexForPoint(int x, int y) {
    return (y * width) + x;
  }

  public void setAt(int x, int y, int t) {
    data[indexForPoint(x, y)] = t;
  }

  public int getAt(int x, int y) {
    return data[indexForPoint(x, y)];
  }

  public void fill(int fillValue) {
    for (int i = 0; i < data.length; i += 1) {
      data[i] = fillValue;
    }
  }

  public byte[] toByteArray() {
    var byteArray = new byte[data.length];
    for (int i = 0; i < data.length; i += 1) {
      byteArray[i] = (byte) data[i];
    }

    return byteArray;
  }

  public void fromByteArray(byte[] readBuffer) {
    for (int i = 0; i < data.length; i += 1) {
      data[i] = readBuffer[i];
    }
  }

  public void mirrorX() {

    var xHalf = width / 2;

    for (int x = 0; x < xHalf; x += 1) {
      for (int y = 0; y < height; y += 1) {
        setAt(width - x - 1, y, getAt(x, y));
      }
    }
  }

  public void forEachField(MapLayerFieldAction action) {
    for (int x = 0; x < width; x += 1) {
      for (int y = 0; y < height; y += 1) {
        action.performFieldActionOnField(x, y, getAt(x, y));
      }
    }
  }
}
