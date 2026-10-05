package com.howtodoinjava.migration;

import lombok.Builder;
import lombok.Value;

/**
 * A recipe with a name and the time it takes to cook, in minutes.
 * Lombok generates the constructor, getters, equals/hashCode, toString and the builder.
 */
@Value
@Builder
public class Recipe {
  String name;
  int minutes;
}
