package com.howtodoinjava.iae;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/** The safe version of each failing call. */
class FixesTest {

  @Test
  void enumLookupReturnsOptional() {
    assertThat(Size.parse(" small ")).contains(Size.SMALL);
    assertThat(Size.parse("XL")).isEmpty();
    assertThat(Size.parse(null)).isEmpty();
    Size size = Size.parse("XL").orElse(Size.MEDIUM);
    assertThat(size).isEqualTo(Size.MEDIUM);
  }

  @Test
  void parseQuantityReturnsOptionalInt() {
    assertThat(SafeInputs.parseQuantity(" 5 ")).isEqualTo(OptionalInt.of(5));
    assertThat(SafeInputs.parseQuantity("abc")).isEmpty();
    assertThat(SafeInputs.parseQuantity("3000000000")).isEmpty();
    long big = Long.parseLong("3000000000");
    assertThat(big).isEqualTo(3_000_000_000L);
  }

  @Test
  void parseUuidReturnsOptional() {
    Optional<UUID> id = SafeInputs.parseUuid("123e4567-e89b-12d3-a456-426614174000");
    assertThat(id).isPresent();
    assertThat(SafeInputs.parseUuid("apple")).isEmpty();
  }

  @Test
  void decodeBase64ReturnsOptional() {
    assertThat(SafeInputs.decodeBase64("YXBwbGU=")).hasValueSatisfying(
        bytes -> assertThat(new String(bytes)).isEqualTo("apple"));
    assertThat(SafeInputs.decodeBase64("abc$")).isEmpty();
  }

  @Test
  void capacityIsClampedToZero() {
    int requested = -1;
    List<String> fruits = new ArrayList<>(SafeInputs.safeCapacity(requested));
    assertThat(fruits).isEmpty();
  }

  @Test
  void randomPickHandlesEmptyList() {
    List<String> stock = new ArrayList<>();
    Random random = new Random();
    Optional<String> pick = stock.isEmpty()
        ? Optional.empty()
        : Optional.of(stock.get(random.nextInt(stock.size())));
    assertThat(pick).isEmpty();
  }

  @Test
  void subListWithOrderedBounds() {
    List<String> fruits = new ArrayList<>(List.of("apple", "banana", "cherry"));
    int from = 2;
    int to = 1;
    List<String> part = fruits.subList(Math.min(from, to), Math.max(from, to));
    assertThat(part).containsExactly("banana");
  }
}
