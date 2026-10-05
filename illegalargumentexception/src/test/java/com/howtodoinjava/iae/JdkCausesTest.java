package com.howtodoinjava.iae;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

/** Each JDK call below throws java.lang.IllegalArgumentException with the asserted message. */
class JdkCausesTest {

  @Test
  void enumValueOfWithUnknownName() {
    assertThatThrownBy(() -> Size.valueOf("XL"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("No enum constant com.howtodoinjava.iae.Size.XL");
  }

  @Test
  void enumValueOfIsCaseSensitiveAndDoesNotTrim() {
    assertThatThrownBy(() -> Size.valueOf("small"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("No enum constant com.howtodoinjava.iae.Size.small");
    assertThatThrownBy(() -> Size.valueOf("SMALL "))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("No enum constant com.howtodoinjava.iae.Size.SMALL ");
  }

  @Test
  void negativeCapacity() {
    assertThatThrownBy(() -> new ArrayList<String>(-1))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("Illegal Capacity: -1");
    assertThatThrownBy(() -> new HashMap<String, Integer>(-1))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("Illegal initial capacity: -1");
  }

  @Test
  void negativeCount() {
    assertThatThrownBy(() -> "ab".repeat(-1))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("count is negative: -1");
    assertThatThrownBy(() -> Collections.nCopies(-1, "apple"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("List length = -1");
  }

  @Test
  void randomBoundZero() {
    assertThatThrownBy(() -> new Random().nextInt(0))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("bound must be positive");
  }

  @Test
  void subListFromGreaterThanTo() {
    List<String> fruits = new ArrayList<>(List.of("apple", "banana", "cherry"));
    assertThatThrownBy(() -> fruits.subList(2, 1))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("fromIndex(2) > toIndex(1)");
  }

  @Test
  void malformedUuid() {
    assertThatThrownBy(() -> UUID.fromString("apple"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid UUID string: apple");
  }

  @Test
  void illegalBase64Character() {
    assertThatThrownBy(() -> Base64.getDecoder().decode("abc$"))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("Illegal base64 character 24");
  }

  @Test
  void threadPoolAndQueueSizes() {
    assertThatThrownBy(() -> Executors.newFixedThreadPool(0))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("maximumPoolSize must be positive");
    assertThatThrownBy(() -> new ArrayBlockingQueue<String>(0))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage(null);
  }

  @Test
  void threadPriorityOutOfRangeHasNoMessage() {
    Thread worker = new Thread(() -> { });
    assertThatThrownBy(() -> worker.setPriority(11))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage(null);
    assertThat(Thread.MAX_PRIORITY).isEqualTo(10);
  }
}
