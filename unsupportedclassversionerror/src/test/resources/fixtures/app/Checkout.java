package com.howtodoinjava;

import com.acme.pricing.TaxCalculator;

public class Checkout {
  public static void main(String[] args) {
    System.out.println("Starting checkout");
    System.out.println("Total: " + TaxCalculator.withTax(100.0));
  }
}
