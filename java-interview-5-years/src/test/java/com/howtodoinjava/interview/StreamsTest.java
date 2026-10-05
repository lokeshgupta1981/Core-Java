package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StreamsTest {

  @Test
  void mapVsFlatMap() {
    List<List<String>> baskets = List.of(List.of("apple", "banana"), List.of("cherry"));
    List<Integer> sizes = baskets.stream().map(List::size).toList();           // [2, 1]
    List<String> fruits = baskets.stream().flatMap(List::stream).toList();     // [apple, banana, cherry]

    assertThat(sizes).containsExactly(2, 1);
    assertThat(fruits).containsExactly("apple", "banana", "cherry");
  }

  @Test
  void streamsAreLazy() {
    List<String> log = new ArrayList<>();
    Optional<String> first = Stream.of("apple", "banana", "cherry")
        .peek(fruit -> log.add("saw " + fruit))
        .filter(fruit -> fruit.startsWith("b"))
        .findFirst();                                    // Optional[banana]
    // log = [saw apple, saw banana], cherry is never read

    assertThat(first).contains("banana");
    assertThat(log).containsExactly("saw apple", "saw banana");
  }

  @Test
  void toMapDuplicates() {
    List<String> fruits = List.of("apple", "avocado", "banana");

    assertThatThrownBy(() -> {
      Map<Character, String> byLetter = fruits.stream()
          .collect(Collectors.toMap(fruit -> fruit.charAt(0), fruit -> fruit));
          // IllegalStateException: Duplicate key a (attempted merging values apple and avocado)
    })
        .isInstanceOf(IllegalStateException.class)
        .satisfies(e -> System.out.println("toMap: " + e));

    Map<Character, String> merged = fruits.stream()
        .collect(Collectors.toMap(fruit -> fruit.charAt(0), fruit -> fruit,
            (first, second) -> first + "," + second));  // {a=apple,avocado, b=banana}
    Map<Character, Long> counts = fruits.stream()
        .collect(Collectors.groupingBy(fruit -> fruit.charAt(0), Collectors.counting()));   // {a=2, b=1}

    assertThat(merged).hasToString("{a=apple,avocado, b=banana}");
    assertThat(counts).hasToString("{a=2, b=1}");
  }
}
