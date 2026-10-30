package com.howtodoinjava.flexibleconstructors;

public class SavingsAccount extends Account {

  private final double interestRate;

  public SavingsAccount(String id, double interestRate) {
    if (interestRate <= 0) {
      throw new IllegalArgumentException("Interest rate must be positive: " + interestRate);
    }
    this.interestRate = interestRate;
    super(id);
  }

  public SavingsAccount(String record) {
    String[] parts = record.split(";");
    String id = parts[0].trim();
    double interestRate = Double.parseDouble(parts[1].trim());
    this(id, interestRate);
  }

  public double interestRate() {
    return interestRate;
  }

  @Override
  public String describe() {
    return "Savings " + id() + " at " + interestRate + "%";
  }
}
