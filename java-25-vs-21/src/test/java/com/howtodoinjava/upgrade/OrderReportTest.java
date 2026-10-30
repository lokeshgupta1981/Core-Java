package com.howtodoinjava.upgrade;

import java.util.List;

import com.howtodoinjava.upgrade.OrderReport.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderReportTest {

  private final List<Order> orders = List.of(
      new Order("A-1", 120), new Order("A-2", 80), new Order("A-3", 45),
      new Order("A-4", 300), new Order("A-5", 60));

  @Test
  void givenFiveOrders_whenBatchingByTwo_thenLastBatchHasOneOrder() {
    List<List<Order>> batches = OrderReport.inBatchesOf(orders, 2);

    assertThat(batches).hasSize(3);
    assertThat(batches.get(2)).containsExactly(new Order("A-5", 60));
  }

  @Test
  void givenFiveOrders_whenCountingWithUnnamedVariable_thenFive() {
    assertThat(OrderReport.countOrders(orders)).isEqualTo(5);
  }
}
