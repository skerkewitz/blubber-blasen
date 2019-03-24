package de.skerkewitz.blubberblase.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JumpUtilsTest {

  @Test
  void calcV0() {

    assertEquals(2.8f, JumpUtils.calcV0(42, 1, 30));

  }

  @Test
  void calcG() {

    assertEquals(-0.093333333f, JumpUtils.calcG(42, 1, 30));
  }
}