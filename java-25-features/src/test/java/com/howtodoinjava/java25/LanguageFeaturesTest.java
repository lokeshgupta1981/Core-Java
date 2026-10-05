package com.howtodoinjava.java25;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.howtodoinjava.java25.language.EBook;
import com.howtodoinjava.java25.language.ModuleImports;
import com.howtodoinjava.java25.language.OldStyleEBook;
import com.howtodoinjava.java25.language.PrimitivePatterns;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class LanguageFeaturesTest {

  @Test
  void fieldIsInitializedBeforeSuperConstructorCallsOverride() {
    assertThat(new EBook("Dune", 412, " epub ").summary()).isEqualTo("Dune (EPUB)");
    assertThat(new OldStyleEBook("Dune", 412, "epub").summary()).isEqualTo("Dune (null)");
  }

  @Test
  void argumentsAreValidatedBeforeSuper() {
    assertThatThrownBy(() -> new EBook("Emma", 0, "pdf"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("pages must be positive: 0");
    assertThatThrownBy(() -> new EBook("Emma", 474, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("format");
  }

  @Test
  void moduleImportGivesAccessToJavaBasePackages() {
    assertThat(ModuleImports.pagesByTitle(List.of("Ulysses", "Dune", "Emma")))
        .containsExactly(
            org.assertj.core.api.Assertions.entry("Dune", 4),
            org.assertj.core.api.Assertions.entry("Emma", 4),
            org.assertj.core.api.Assertions.entry("Ulysses", 7));
    assertThat(ModuleImports.catalogFile()).isEqualTo(Path.of("catalog/books.csv"));
  }

  @Test
  void primitivePatternsInSwitch() {
    assertThat(PrimitivePatterns.size(0)).isEqualTo("empty");
    assertThat(PrimitivePatterns.size(80)).isEqualTo("short");
    assertThat(PrimitivePatterns.size(412)).isEqualTo("medium");
    assertThat(PrimitivePatterns.size(1225)).isEqualTo("long (1225 pages)");
    assertThat(PrimitivePatterns.rating(5.0)).isEqualTo("perfect");
    assertThat(PrimitivePatterns.rating(4.2)).isEqualTo("good");
    assertThat(PrimitivePatterns.rating(2.5)).isEqualTo("read reviews first (2.5)");
    assertThat(PrimitivePatterns.availability(true)).isEqualTo("ready to borrow");
    assertThat(PrimitivePatterns.availability(false)).isEqualTo("join the waiting list");
  }

  @Test
  void primitiveInstanceofChecksForLossyConversion() {
    assertThat(PrimitivePatterns.toIntSafely(150_000L)).isEqualTo("fits in int: 150000");
    assertThat(PrimitivePatterns.toIntSafely(3_000_000_000L)).isEqualTo("too large for int: 3000000000");
    assertThat(PrimitivePatterns.fitsInByte(100)).isTrue();
    assertThat(PrimitivePatterns.fitsInByte(300)).isFalse();
  }
}
