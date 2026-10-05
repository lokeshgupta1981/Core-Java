package com.howtodoinjava.gradle9;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FruitBasketTest {

  @Test
  void addsCountsOfTheSameFruit() {
    FruitBasket basket = new FruitBasket();
    basket.add("apple", 5);
    basket.add("apple", 2);

    assertEquals(7, basket.countOf("apple"));
  }

  @Test
  void totalSumsAllFruits() {
    FruitBasket basket = new FruitBasket();
    basket.add("apple", 5);
    basket.add("banana", 3);

    assertEquals(8, basket.total());
  }

  @Test
  void missingFruitHasZeroCount() {
    FruitBasket basket = new FruitBasket();

    assertEquals(0, basket.countOf("cherry"));
  }

  @ParameterizedTest
  @CsvSource({"apple, 0", "apple, -1", "' ', 3"})
  void rejectsBadInput(String fruit, int count) {
    FruitBasket basket = new FruitBasket();

    assertThrows(IllegalArgumentException.class, () -> basket.add(fruit, count));
  }
}
