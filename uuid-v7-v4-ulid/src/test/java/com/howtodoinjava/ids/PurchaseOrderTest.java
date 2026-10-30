package com.howtodoinjava.ids;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseOrderTest {

  @Test
  void whenCreatingOrders_thenIdIsVersion7AndOrdersSortByCreation() {
    PurchaseOrder first = new PurchaseOrder(40);
    PurchaseOrder second = new PurchaseOrder(25);

    assertThat(first.getId().version()).isEqualTo(7);
    assertThat(first.getId()).isLessThan(second.getId());
  }
}
