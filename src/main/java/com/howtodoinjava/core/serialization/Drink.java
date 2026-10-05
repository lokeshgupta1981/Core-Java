package com.howtodoinjava.core.serialization;

import java.io.Serial;
import java.io.Serializable;

/**
 * A Serializable child of {@link MenuItemNoDefault}. Writing works; reading fails because
 * the parent has no accessible no-arg constructor.
 */
public class Drink extends MenuItemNoDefault implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final int ml;

  public Drink(String name, int ml) {
    super(name);
    this.ml = ml;
  }
}
