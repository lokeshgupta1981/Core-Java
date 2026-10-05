package com.howtodoinjava.iae;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.junit.jupiter.api.Test;

/** Subclasses of IllegalArgumentException, and look-alikes that are NOT subclasses. */
class HierarchyTest {

  @Test
  void iaeIsAnUncheckedRuntimeException() {
    assertThat(RuntimeException.class).isAssignableFrom(IllegalArgumentException.class);
  }

  @Test
  void numberFormatExceptionIsASubclass() {
    assertThatThrownBy(() -> Integer.parseInt("abc"))
        .isExactlyInstanceOf(NumberFormatException.class)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("For input string: \"abc\"");
    assertThatThrownBy(() -> Integer.parseInt("3000000000"))
        .isExactlyInstanceOf(NumberFormatException.class)
        .hasMessage("For input string: \"3000000000\"");
  }

  @Test
  void patternSyntaxExceptionIsASubclass() {
    assertThatThrownBy(() -> Pattern.compile("(apple"))
        .isExactlyInstanceOf(PatternSyntaxException.class)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unclosed group near index 6" + System.lineSeparator() + "(apple");
  }

  @Test
  void formatExceptionsAreSubclasses() {
    assertThatThrownBy(() -> String.format("%d", "apple"))
        .isExactlyInstanceOf(IllegalFormatConversionException.class)
        .isInstanceOf(IllegalFormatException.class)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("d != java.lang.String");
  }

  @Test
  void charsetExceptionsAreSubclasses() {
    assertThatThrownBy(() -> Charset.forName("utf-99"))
        .isExactlyInstanceOf(UnsupportedCharsetException.class)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("utf-99");
    assertThatThrownBy(() -> Charset.forName("a b"))
        .isExactlyInstanceOf(IllegalCharsetNameException.class)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("a b");
  }

  @Test
  void invalidPathExceptionIsASubclass() {
    assertThatThrownBy(() -> Path.of("apple\0.txt"))
        .isExactlyInstanceOf(InvalidPathException.class)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageStartingWith("Nul character not allowed");
  }

  @Test
  void startingAThreadTwice() throws InterruptedException {
    Thread worker = new Thread(() -> { });
    worker.start();
    worker.join();
    assertThatThrownBy(worker::start)
        .isExactlyInstanceOf(IllegalThreadStateException.class)
        .isInstanceOf(IllegalArgumentException.class)
        .isNotInstanceOf(IllegalStateException.class)
        .hasMessage(null);
  }

  @Test
  void illegalThreadStateExceptionIsASubclassToo() {
    assertThat(IllegalArgumentException.class).isAssignableFrom(IllegalThreadStateException.class);
    assertThat(IllegalStateException.class.isAssignableFrom(IllegalThreadStateException.class)).isFalse();
  }

  @Test
  void dateTimeExceptionIsNotASubclass() {
    assertThatThrownBy(() -> LocalDate.of(2026, 2, 30))
        .isExactlyInstanceOf(DateTimeException.class)
        .isNotInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid date 'FEBRUARY 30'");
    assertThatThrownBy(() -> Month.of(13))
        .isExactlyInstanceOf(DateTimeException.class)
        .hasMessage("Invalid value for MonthOfYear: 13");
    assertThatThrownBy(() -> Duration.parse("10s"))
        .isExactlyInstanceOf(DateTimeParseException.class)
        .isNotInstanceOf(IllegalArgumentException.class)
        .hasMessage("Text cannot be parsed to a Duration");
  }

  @Test
  void checkIndexAndRequireNonNullAreNotIae() {
    assertThatThrownBy(() -> Objects.checkIndex(5, 3))
        .isExactlyInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 5 out of bounds for length 3");
    assertThatThrownBy(() -> Objects.requireNonNull(null, "fruit must not be null"))
        .isExactlyInstanceOf(NullPointerException.class)
        .hasMessage("fruit must not be null");
  }
}
