package com.howtodoinjava.flexibleconstructors;

public class LegacySavingsAccount extends Account {

  private final double interestRate;

  public LegacySavingsAccount(String id, double interestRate) {
    super(id);
    if (interestRate <= 0) {
      throw new IllegalArgumentException("Interest rate must be positive: " + interestRate);
    }
    this.interestRate = interestRate;
  }

  @Override
  public String describe() {
    return "Savings " + id() + " at " + interestRate + "%";
  }
}
