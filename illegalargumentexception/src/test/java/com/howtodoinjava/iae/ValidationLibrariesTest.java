package com.howtodoinjava.iae;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/** Spring's Assert and Guava's Preconditions, and the exception each method throws. */
class ValidationLibrariesTest {

  @Test
  void springAssertIsTrueThrowsIae() {
    int quantity = -5;
    assertThatThrownBy(() -> Assert.isTrue(quantity > 0, "quantity must be positive"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("quantity must be positive");
  }

  @Test
  void springAssertNotNullThrowsIaeNotNpe() {
    String fruit = null;
    assertThatThrownBy(() -> Assert.notNull(fruit, "fruit must not be null"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("fruit must not be null");
  }

  @Test
  void springAssertHasTextThrowsIae() {
    String fruit = " ";
    assertThatThrownBy(() -> Assert.hasText(fruit, "fruit must not be blank"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("fruit must not be blank");
  }

  @Test
  void springAssertStateThrowsIse() {
    boolean closed = true;
    assertThatThrownBy(() -> Assert.state(!closed, "basket is closed"))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("basket is closed");
  }

  @Test
  void guavaCheckArgumentThrowsIae() {
    int quantity = -5;
    assertThatThrownBy(() -> Preconditions.checkArgument(quantity > 0, "quantity must be positive: %s", quantity))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("quantity must be positive: -5");
  }

  @Test
  void guavaCheckNotNullThrowsNpe() {
    String fruit = null;
    assertThatThrownBy(() -> Preconditions.checkNotNull(fruit, "fruit must not be null"))
        .isExactlyInstanceOf(NullPointerException.class)
        .hasMessage("fruit must not be null");
  }

  @Test
  void guavaCheckStateThrowsIse() {
    boolean closed = true;
    assertThatThrownBy(() -> Preconditions.checkState(!closed, "basket is closed"))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("basket is closed");
  }

  @Test
  void guavaCheckElementIndexThrowsIndexOutOfBounds() {
    assertThatThrownBy(() -> Preconditions.checkElementIndex(5, 3))
        .isExactlyInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("index (5) must be less than size (3)");
  }
}
