package com.howtodoinjava.classversion;

/** A small service mocked in PriceServiceTest to show that Mockito (Byte Buddy) can subclass Java 25 classes. */
public class PriceService {

  private final PriceRepository repository;

  public PriceService(PriceRepository repository) {
    this.repository = repository;
  }

  public int priceWithTax(String fruit) {
    int price = repository.priceOf(fruit);
    return price + price / 10;
  }

  public static class PriceRepository {
    public int priceOf(String fruit) {
      throw new UnsupportedOperationException("needs a database");
    }
  }
}
