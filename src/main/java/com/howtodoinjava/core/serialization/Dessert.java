package com.howtodoinjava.core.serialization;

import java.io.Serial;
import java.io.Serializable;

/**
 * A Serializable child of the non-serializable {@link MenuItem}. Only the Dessert fields
 * are written; the name field comes from the MenuItem no-arg constructor after reading.
 */
public class Dessert extends MenuItem implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final int sugarGrams;

  public Dessert(String name, int sugarGrams) {
    super(name);
    this.sugarGrams = sugarGrams;
  }

  @Override
  public String toString() {
    return "Dessert[name=" + name + ", sugarGrams=" + sugarGrams + "]";
  }
}
