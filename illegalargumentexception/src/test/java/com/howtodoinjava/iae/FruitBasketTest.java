package com.howtodoinjava.iae;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** Which exception our own guard clauses throw for each kind of problem. */
class FruitBasketTest {

  @Test
  void validCallsWork() {
    FruitBasket basket = new FruitBasket(10);
    basket.add("apple", 5);
    assertThat(basket.size()).isEqualTo(5);
    assertThat(basket.get(0)).isEqualTo("apple");
  }

  @Test
  void wrongCapacityIsIae() {
    assertThatThrownBy(() -> new FruitBasket(0))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("capacity must be positive: 0");
  }

  @Test
  void nullArgumentIsNpe() {
    FruitBasket basket = new FruitBasket(10);
    assertThatThrownBy(() -> basket.add(null, 5))
        .isExactlyInstanceOf(NullPointerException.class)
        .hasMessage("fruit must not be null");
  }

  @Test
  void wrongValuesAreIae() {
    FruitBasket basket = new FruitBasket(10);
    assertThatThrownBy(() -> basket.add(" ", 5))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("fruit must not be blank");
    assertThatThrownBy(() -> basket.add("apple", -5))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("quantity must be positive: -5");
  }

  @Test
  void wrongStateIsIse() {
    FruitBasket basket = new FruitBasket(10);
    basket.close();
    assertThatThrownBy(() -> basket.add("apple", 5))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("basket is closed");
  }

  @Test
  void fullBasketIsIse() {
    FruitBasket basket = new FruitBasket(10);
    basket.add("apple", 8);
    assertThatThrownBy(() -> basket.add("banana", 3))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("basket is full: 8/10");
  }

  @Test
  void badIndexIsIndexOutOfBounds() {
    FruitBasket basket = new FruitBasket(10);
    basket.add("apple", 3);
    assertThatThrownBy(() -> basket.get(5))
        .isExactlyInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 5 out of bounds for length 3");
  }
}
