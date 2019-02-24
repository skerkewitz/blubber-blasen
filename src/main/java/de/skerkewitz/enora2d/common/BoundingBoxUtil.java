package de.skerkewitz.enora2d.common;

public class BoundingBoxUtil {

  private BoundingBoxUtil() {
    /* No instance allowed. */
  }

  public static boolean doesOverlap(Rect2i tbb, Rect2i obb) {
    return !((tbb.origin.x + tbb.size.width < obb.origin.x) || (obb.origin.x + obb.size.width < tbb.origin.x)
            || (tbb.origin.y + tbb.size.height < obb.origin.y) || (obb.origin.y + obb.size.height < tbb.origin.y));
  }
}
