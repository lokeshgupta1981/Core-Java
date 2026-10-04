package com.howtodoinjava.core.collections.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Shows the cost of a bad hashCode(): every key lands in one bucket. Since Java 8 the bucket becomes a
 * red-black tree, and Comparable keys let the tree find a key quickly. Timings vary between machines.
 */
public class HashMapKeyCollisions {

  /** Good hashCode(): spread over all buckets. */
  record GoodKey(int id) {
  }

  /** Constant hashCode(), not Comparable. */
  record BadKey(int id) {
    @Override
    public int hashCode() {
      return 42;
    }
  }

  /** Constant hashCode(), but Comparable, so a treeified bucket can search by compareTo(). */
  record BadComparableKey(int id) implements Comparable<BadComparableKey> {
    @Override
    public int hashCode() {
      return 42;
    }

    @Override
    public int compareTo(BadComparableKey other) {
      return Integer.compare(id, other.id);
    }
  }

  static final int KEYS = 20_000;

  static <K> long millisToFill(IntFunction<K> factory) {
    long best = Long.MAX_VALUE;
    for (int round = 0; round < 5; round++) {
      long start = System.nanoTime();
      Map<K, Integer> map = new HashMap<>();
      for (int i = 0; i < KEYS; i++) {
        map.put(factory.apply(i), i);
      }
      for (int i = 0; i < KEYS; i++) {
        if (map.get(factory.apply(i)) != i) {
          throw new IllegalStateException("lookup failed");
        }
      }
      best = Math.min(best, (System.nanoTime() - start) / 1_000_000);
    }
    return best;
  }

  public static void main(String[] args) {
    System.out.println("Put + get " + KEYS + " keys (best of 5 runs):");
    System.out.println("GoodKey           " + millisToFill(GoodKey::new) + " ms");
    System.out.println("BadComparableKey  " + millisToFill(BadComparableKey::new) + " ms");
    System.out.println("BadKey            " + millisToFill(BadKey::new) + " ms");
  }
}
