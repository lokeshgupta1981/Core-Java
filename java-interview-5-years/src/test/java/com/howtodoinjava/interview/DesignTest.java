package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;

import com.howtodoinjava.interview.design.Config;
import com.howtodoinjava.interview.design.LruCache;
import com.howtodoinjava.interview.design.TokenBucket;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

class DesignTest {

  @Test
  void singleton() {
    boolean same = Config.getInstance() == Config.getInstance();   // true
    assertThat(same).isTrue();
  }

  @Test
  void lruCache() {
    LruCache<String, Integer> cache = new LruCache<>(2);
    cache.put("apple", 5);
    cache.put("banana", 3);
    Integer apple = cache.get("apple");                  // 5, apple is now the most recently used
    cache.put("cherry", 7);                              // evicts banana
    Set<String> keys = cache.keySet();                   // [apple, cherry]

    assertThat(apple).isEqualTo(5);
    assertThat(keys).containsExactly("apple", "cherry");
  }

  @Test
  void tokenBucket() {
    TokenBucket limiter = new TokenBucket(2, 1, System::nanoTime);   // burst of 2, then 1 per second
    boolean first = limiter.tryAcquire();                // true
    boolean second = limiter.tryAcquire();               // true
    boolean third = limiter.tryAcquire();                // false, bucket empty: answer HTTP 429

    assertThat(first).isTrue();
    assertThat(second).isTrue();
    assertThat(third).isFalse();

    AtomicLong now = new AtomicLong();
    TokenBucket fake = new TokenBucket(2, 1, now::get);
    fake.tryAcquire();
    fake.tryAcquire();
    assertThat(fake.tryAcquire()).isFalse();
    now.addAndGet(1_000_000_000L);                       // one second later
    assertThat(fake.tryAcquire()).isTrue();
  }
}
