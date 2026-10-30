package com.howtodoinjava.ids;

import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PurchaseOrder {

  @Id
  private UUID id;

  private int total;

  protected PurchaseOrder() {
  }

  public PurchaseOrder(int total) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.total = total;
  }

  public UUID getId() {
    return id;
  }

  public int getTotal() {
    return total;
  }
}
