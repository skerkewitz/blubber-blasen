package de.skerkewitz.blubberblase.entity;

/**
 * Class to help with the math regarding jumping.
 */
public interface JumpUtils {

  static float calcV0(float maxHeight, float maxHorizontalVelocity, float maxHalfDistance) {
    return (2 * maxHeight * maxHorizontalVelocity) / maxHalfDistance;
  }

  static float calcG(float maxHeight, float maxHorizontalVelocity, float maxHalfDistance) {
    return (-2 * maxHeight * (maxHorizontalVelocity * maxHorizontalVelocity)) / (maxHalfDistance * maxHalfDistance);
  }

}
