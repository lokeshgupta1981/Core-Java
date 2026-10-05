package com.howtodoinjava.iae;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A small basket that validates its arguments with guard clauses.
 * It throws:
 * - NullPointerException when a required argument is null,
 * - IllegalArgumentException when an argument has a wrong value,
 * - IllegalStateException when the basket is in the wrong state for the call,
 * - IndexOutOfBoundsException when an index is outside the basket.
 */
public class FruitBasket {

  private final int capacity;
  private final List<String> fruits = new ArrayList<>();
  private boolean closed;

  public FruitBasket(int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity must be positive: " + capacity);
    }
    this.capacity = capacity;
  }

  public void add(String fruit, int quantity) {
    Objects.requireNonNull(fruit, "fruit must not be null");
    if (fruit.isBlank()) {
      throw new IllegalArgumentException("fruit must not be blank");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be positive: " + quantity);
    }
    if (closed) {
      throw new IllegalStateException("basket is closed");
    }
    if (fruits.size() + quantity > capacity) {
      throw new IllegalStateException("basket is full: " + fruits.size() + "/" + capacity);
    }
    for (int i = 0; i < quantity; i++) {
      fruits.add(fruit.strip());
    }
  }

  public String get(int index) {
    Objects.checkIndex(index, fruits.size());
    return fruits.get(index);
  }

  public void close() {
    closed = true;
  }

  public int size() {
    return fruits.size();
  }
}
