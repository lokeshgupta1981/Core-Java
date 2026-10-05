package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.howtodoinjava.interview.collections.Person;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class CollectionsTest {

  @Test
  void mutableKeyIsLost() {
    Map<Person, Integer> ages = new HashMap<>();
    Person lokesh = new Person("Lokesh");
    ages.put(lokesh, 37);
    lokesh.setName("Alex");                              // hashCode() changes
    Integer age = ages.get(lokesh);                      // null
    boolean present = ages.containsValue(37);            // true, the entry is still there

    assertThat(age).isNull();
    assertThat(present).isTrue();
  }

  @Test
  void concurrentHashMapMerge() {
    Map<String, Integer> counts = new ConcurrentHashMap<>();
    try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
      for (int i = 0; i < 1_000; i++) {
        pool.submit(() -> counts.merge("apple", 1, Integer::sum));
      }
    }
    int apples = counts.get("apple");                    // 1000
    assertThat(apples).isEqualTo(1_000);
    assertThatThrownBy(() -> {
      Integer old = counts.put("banana", null);          // NullPointerException
    })
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void failFastIterator() {
    List<String> fruits = new ArrayList<>(List.of("apple", "banana", "cherry"));
    assertThatThrownBy(() -> {
      for (String fruit : fruits) {
        if (fruit.startsWith("a")) {
          fruits.remove(fruit);                          // ConcurrentModificationException
        }
      }
    }).isInstanceOf(ConcurrentModificationException.class);

    List<String> more = new ArrayList<>(List.of("apple", "banana", "cherry"));
    boolean removed = more.removeIf(fruit -> fruit.startsWith("a"));   // true, more = [banana, cherry]
    assertThat(removed).isTrue();
    assertThat(more).containsExactly("banana", "cherry");
  }

  @Test
  void removingSecondToLastDoesNotThrow() {
    List<String> fruits = new ArrayList<>(List.of("apple", "banana", "cherry"));
    for (String fruit : fruits) {
      if (fruit.startsWith("b")) {
        fruits.remove(fruit);                            // no exception: hasNext() is false
      }
    }
    assertThat(fruits).containsExactly("apple", "cherry");
  }
}
