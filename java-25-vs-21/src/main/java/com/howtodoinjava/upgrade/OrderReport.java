package com.howtodoinjava.upgrade;

import java.util.List;
import java.util.stream.Gatherers;

/**
 * Code that compiles on Java 25 but not on Java 21.
 */
public class OrderReport {

  public record Order(String id, int amount) { }

  // Stream gatherers: final in Java 24 (JEP 485)
  public static List<List<Order>> inBatchesOf(List<Order> orders, int size) {
    return orders.stream()
        .gather(Gatherers.windowFixed(size))
        .toList();
  }

  // Unnamed variables: final in Java 22 (JEP 456)
  public static int countOrders(List<Order> orders) {
    int count = 0;
    for (Order _ : orders) {
      count++;
    }
    return count;
  }
}
