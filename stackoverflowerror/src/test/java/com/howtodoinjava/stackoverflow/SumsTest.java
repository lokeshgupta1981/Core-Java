package com.howtodoinjava.stackoverflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SumsTest {

  @Test
  void missingBaseCaseOverflowsTheStack() {
    assertThrows(StackOverflowError.class, () -> Sums.sumToNoBase(5));
  }

  @Test
  void baseCaseStopsTheRecursion() {
    assertEquals(15, Sums.sumTo(5));
  }

  @Test
  void correctRecursionStillOverflowsWhenTooDeep() {
    assertThrows(StackOverflowError.class, () -> Sums.sumTo(1_000_000));
  }

  @Test
  void tailRecursionIsNotOptimized() {
    assertEquals(15, Sums.sumToTail(5, 0));
    assertThrows(StackOverflowError.class, () -> Sums.sumToTail(1_000_000, 0));
  }

  @Test
  void loopHasNoDepthLimit() {
    assertEquals(500_000_500_000L, Sums.sumToLoop(1_000_000));
  }
}
