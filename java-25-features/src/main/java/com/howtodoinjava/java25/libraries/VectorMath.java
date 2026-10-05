package com.howtodoinjava.java25.libraries;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

/** JEP 508 (tenth incubator): Vector API. */
public class VectorMath {

  private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

  /** Multiplies prices by quantities, element by element. */
  public static float[] multiply(float[] prices, float[] quantities) {
    float[] totals = new float[prices.length];
    VectorSpecies<Float> species = FloatVector.SPECIES_PREFERRED;
    int i = 0;
    for (; i < species.loopBound(prices.length); i += species.length()) {
      FloatVector p = FloatVector.fromArray(species, prices, i);
      FloatVector q = FloatVector.fromArray(species, quantities, i);
      p.mul(q).intoArray(totals, i);
    }
    for (; i < prices.length; i++) {          // tail that does not fill a whole vector
      totals[i] = prices[i] * quantities[i];
    }
    return totals;
  }

  public static int lanes() {
    return SPECIES.length();
  }
}
