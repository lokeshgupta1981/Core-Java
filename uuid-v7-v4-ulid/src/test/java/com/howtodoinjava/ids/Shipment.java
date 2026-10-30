package com.howtodoinjava.ids;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.hibernate.annotations.UuidGenerator;

@Entity
public class Shipment {

  @Id
  @GeneratedValue
  @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
  private UUID id;

  private String carrier;

  protected Shipment() {
  }

  public Shipment(String carrier) {
    this.carrier = carrier;
  }

  public UUID getId() {
    return id;
  }
}
