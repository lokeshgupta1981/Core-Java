package com.howtodoinjava.interview.modern;


public final class Fees {

  private Fees() {
  }

  /** Exhaustive switch over a sealed hierarchy; no default branch needed. */
  public static int fee(Payment payment) {
    return switch (payment) {
      case Card(int amount) -> amount * 2 / 100;
      case Cash _ -> 0;
      case Voucher(int amount, boolean expired) when expired ->
          throw new IllegalStateException("voucher expired");
      case Voucher _ -> 0;
    };
  }

  /** Pattern matching for switch on Object, including the null case. */
  public static String describe(Object value) {
    return switch (value) {
      case null -> "null";
      case Integer i when i > 100 -> "big int " + i;
      case Integer i -> "int " + i;
      case String s -> "string of length " + s.length();
      default -> "other";
    };
  }
}
