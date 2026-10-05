package com.howtodoinjava.moduleimport;

// One module import replaces java.util.*, java.util.function.*,
// java.util.stream.* and every other package that java.base exports.
import module java.base;

public class FruitStats {

  // Groups fruit names by their first letter: {a=[apple, avocado], b=[banana]}
  public static Map<Character, List<String>> byFirstLetter(List<String> fruits) {
    return fruits.stream()
        .collect(Collectors.groupingBy(f -> f.charAt(0), TreeMap::new, Collectors.toList()));
  }

  // Counts the fruit names in a text file, one name per line
  public static long countLines(Path file) throws IOException {
    try (Stream<String> lines = Files.lines(file)) {
      return lines.filter(Predicate.not(String::isBlank)).count();
    }
  }

  public static void main(String[] args) {
    System.out.println(byFirstLetter(List.of("apple", "banana", "avocado")));
  }
}
