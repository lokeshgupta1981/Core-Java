package com.howtodoinjava.core.array;

import java.util.Comparator;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;

import com.howtodoinjava.core.sorting.User;

public class CheckIsSorted {
  public static void main(final String[] args) {
    int[] array = { 1, 2, 3, 4, 5 };

    // 1
    boolean isSorted = checkIsSortedPrimitiveArrayWithStream(array);
    System.out.println(isSorted);

    // 2
    isSorted = checkIsSortedObjectArrayWithStream(ArrayUtils.toObject(array));
    System.out.println(isSorted);

    // 3
    User[] users = getSortedArray();
    Comparator<User> firstNameSorter = Comparator.comparing(User::firstName);
    isSorted = checkIsSortedObjectArrayForCustomSort(users, firstNameSorter);
    System.out.println(isSorted);
    
    //4
    isSorted = ArrayUtils.isSorted(array);
    System.out.println(isSorted);
  }

  public static boolean checkIsSortedPrimitiveArrayWithStream(
    final int[] array) {
    // base case
    if (array == null || array.length <= 1) {
      return true;
    }
    return IntStream.range(0, array.length - 1)
      .noneMatch(i -> array[i] > array[i + 1]);
  }

  public static <T extends Comparable<? super T>> 
    boolean checkIsSortedObjectArrayWithStream(final T[] array) {
    if (array.length <= 1) {
      return true;
    }
    return IntStream.range(0, array.length - 1)
      .noneMatch(i -> array[i].compareTo(array[i + 1]) > 0);
  }

  public static <T> boolean checkIsSortedObjectArrayForCustomSort(
    final T[] array, final Comparator<T> comparator) {
    if (comparator == null) {
      throw new IllegalArgumentException("Comparator should not be null.");
    }
    if (array.length <= 1) {
      return true;
    }
    return IntStream.range(0, array.length - 1)
      .noneMatch(i -> comparator.compare(array[i], array[i + 1]) > 0);
  }

  private static User[] getSortedArray() {
    return new User[] { new User(1L, "A", "Q", 24), new User(4L, "B", "P", 22),
        new User(2L, "C", "O", 27), new User(3L, "D", "N", 29),
        new User(5L, "E", "M", 25) };
  }
}
