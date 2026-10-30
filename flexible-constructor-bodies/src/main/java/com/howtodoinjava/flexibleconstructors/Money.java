package com.howtodoinjava.flexibleconstructors;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {

  public Money(String text) {
    String[] parts = text.trim().split("\\s+");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Expected '<amount> <currency>': " + text);
    }
    this(new BigDecimal(parts[0]), parts[1]);
  }
}
