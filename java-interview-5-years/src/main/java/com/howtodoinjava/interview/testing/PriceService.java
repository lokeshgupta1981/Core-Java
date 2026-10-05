package com.howtodoinjava.interview.testing;

public class PriceService {

  private final TaxClient taxClient;

  public PriceService(TaxClient taxClient) {   // constructor injection
    this.taxClient = taxClient;
  }

  public int finalPrice(int net, String country) {
    return net + net * taxClient.taxPercent(country) / 100;
  }
}
