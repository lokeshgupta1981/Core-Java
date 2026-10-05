package com.howtodoinjava.moduleimport;

import module java.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class FruitStatsTest {

  @Test
  void groupsWithTypesFromSeveralJavaBasePackages() {
    Map<Character, List<String>> groups = FruitStats.byFirstLetter(List.of("apple", "banana", "avocado"));

    assertThat(groups).hasToString("{a=[apple, avocado], b=[banana]}");
  }

  @Test
  void readsFileWithNioTypes(@TempDir Path dir) throws IOException {
    Path file = Files.writeString(dir.resolve("fruits.txt"), "apple\n\nbanana\n");

    assertThat(FruitStats.countLines(file)).isEqualTo(2);
  }
}
