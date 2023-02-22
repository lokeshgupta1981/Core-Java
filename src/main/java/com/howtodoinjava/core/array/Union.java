package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;

public class Union {

  public static void main(String[] args) {
    Integer[] arr1 = {0, 2};
    Integer[] arr2 = {1, 3};

    HashSet<Integer> set = new HashSet<>();

    set.addAll(Arrays.asList(arr1));
    set.addAll(Arrays.asList(arr2));

    Assertions.assertEquals(Set.of(0, 1, 2, 3), set);

    //convert to array, if needed
    Integer[] union = set.toArray(new Integer[0]);

    Assertions.assertArrayEquals(new Integer[]{0, 1, 2, 3}, union);

    //2

    union = Stream.of(arr1, arr2).flatMap(Stream::of).toArray(Integer[]::new);

  }
}
