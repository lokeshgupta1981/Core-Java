package com.howtodoinjava.flexibleconstructors;

public class HelperSavingsAccount extends Account {

  private final double interestRate;

  public HelperSavingsAccount(String id, double interestRate) {
    super(checkRate(id, interestRate));
    this.interestRate = interestRate;
  }

  private static String checkRate(String id, double interestRate) {
    if (interestRate <= 0) {
      throw new IllegalArgumentException("Interest rate must be positive: " + interestRate);
    }
    return id;
  }

  public double interestRate() {
    return interestRate;
  }
}
