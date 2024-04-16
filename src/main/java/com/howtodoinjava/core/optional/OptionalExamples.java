package com.howtodoinjava.core.optional;

import java.util.Optional;

public class OptionalExamples {

  public static void main(String[] args) {
    Optional<String> optionalValue = Optional.of("Hello");
    String result = optionalValue.orElse( generateDefaultValue() );	// Using orElse()
    System.out.println(STR."Result using orElse(): \{result}");

    result = optionalValue.orElseGet(() -> generateDefaultValue() );  // Using orElseGet()
    System.out.println(STR."Result using orElseGet(): \{result}");
  }

  public static String generateDefaultValue() {
    System.out.println("Generating Default Value");
    return "Default Value";
  }
}
