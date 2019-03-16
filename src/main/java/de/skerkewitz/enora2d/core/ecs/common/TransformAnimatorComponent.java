package de.skerkewitz.enora2d.core.ecs.common;

import com.badlogic.gdx.math.Interpolation;
import de.skerkewitz.enora2d.common.Point2f;
import de.skerkewitz.enora2d.core.ecs.Component;

public class TransformAnimatorComponent implements Component {

  public final Point2f startPosition;
  public final Point2f endPosition;
  public final int startFrameCount;
  public final int endFrameCount;
  public final Interpolation interpolation;

  public TransformAnimatorComponent(Point2f startPosition, Point2f endPosition, int startFrameCount, int endFrameCount, Interpolation interpolation) {
    this.startPosition = startPosition.cloneCopy();
    this.endPosition = endPosition.cloneCopy();
    this.startFrameCount = startFrameCount;
    this.endFrameCount = endFrameCount;
    this.interpolation = interpolation;
  }
}
