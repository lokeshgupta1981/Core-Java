package com.howtodoinjava.moduleimport;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FruitColorsTest {

  @Test
  void singleTypeImportResolvesListToJavaUtilList() {
    Map<String, Color> colors = FruitColors.colors(List.of("apple", "banana"));

    assertThat(colors).containsEntry("apple", Color.RED).containsEntry("banana", Color.YELLOW);
  }
}
