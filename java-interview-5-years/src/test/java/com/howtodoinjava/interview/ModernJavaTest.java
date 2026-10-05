package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.howtodoinjava.interview.modern.Fees;
import com.howtodoinjava.interview.modern.Fruit;
import com.howtodoinjava.interview.modern.Payment;
import com.howtodoinjava.interview.modern.Card;
import com.howtodoinjava.interview.modern.Cash;
import com.howtodoinjava.interview.modern.Voucher;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class ModernJavaTest {

  static final ScopedValue<String> USER = ScopedValue.newInstance();

  @Test
  void records() {
    Fruit apple = new Fruit("apple", 5);
    String text = apple.toString();                      // Fruit[name=apple, count=5]
    boolean same = apple.equals(new Fruit("apple", 5));  // true
    int count = apple.count();                           // 5

    assertThat(text).isEqualTo("Fruit[name=apple, count=5]");
    assertThat(same).isTrue();
    assertThat(count).isEqualTo(5);
    assertThatThrownBy(() -> {
      Fruit bad = new Fruit("banana", -1);                 // IllegalArgumentException: count must be >= 0
    })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("count must be >= 0");
  }

  @Test
  void sealedAndSwitch() {
    Payment payment = new Card(500);
    int fee = switch (payment) {
      case Card(int amount) -> amount * 2 / 100;
      case Cash _ -> 0;
      case Voucher(int amount, boolean expired) when expired ->
          throw new IllegalStateException("voucher expired");
      case Voucher _ -> 0;
    };                                                   // 10
    assertThat(fee).isEqualTo(10);
    assertThat(Fees.fee(new Cash(500))).isZero();
    assertThatThrownBy(() -> Fees.fee(new Voucher(500, true)))
        .isInstanceOf(IllegalStateException.class).hasMessage("voucher expired");

    Object value = 150;
    String label = switch (value) {
      case null -> "null";
      case Integer i when i > 100 -> "big int " + i;
      case Integer i -> "int " + i;
      case String s -> "string of length " + s.length();
      default -> "other";
    };                                                   // big int 150
    assertThat(label).isEqualTo("big int 150");
    assertThat(Fees.describe(null)).isEqualTo("null");
    assertThat(Fees.describe("kiwi")).isEqualTo("string of length 4");
  }

  @Test
  void virtualThreads() throws Exception {
    long start = System.nanoTime();
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<Future<Integer>> futures = IntStream.range(0, 10_000)
          .mapToObj(i -> executor.submit(() -> {
            LockSupport.parkNanos(Duration.ofSeconds(1).toNanos());   // blocks like an I/O call
            return i;
          }))
          .toList();
      int done = futures.size();                         // 10000
      assertThat(done).isEqualTo(10_000);
    }                                                    // close() waits for all tasks
    long millis = Duration.ofNanos(System.nanoTime() - start).toMillis();
    System.out.println("10,000 virtual threads finished in " + millis + " ms");
    assertThat(millis).isLessThan(5_000);

    Thread vt = Thread.ofVirtual().name("worker").start(() -> { });
    boolean virtual = vt.isVirtual();                    // true
    vt.join();
    assertThat(virtual).isTrue();
  }

  @Test
  void scopedValues() {
    String greeting = ScopedValue.where(USER, "Lokesh")
        .call(() -> "Hello " + USER.get());              // Hello Lokesh
    boolean bound = USER.isBound();                      // false, outside the scope

    assertThat(greeting).isEqualTo("Hello Lokesh");
    assertThat(bound).isFalse();
  }

  @Test
  void sequencedCollections() {
    List<String> fruits = List.of("apple", "banana", "cherry");
    String first = fruits.getFirst();                    // apple
    String last = fruits.getLast();                      // cherry
    List<String> reversed = fruits.reversed();           // [cherry, banana, apple]

    assertThat(first).isEqualTo("apple");
    assertThat(last).isEqualTo("cherry");
    assertThat(reversed).containsExactly("cherry", "banana", "apple");
  }
}
