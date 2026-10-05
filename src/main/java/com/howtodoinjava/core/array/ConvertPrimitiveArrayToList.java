package com.howtodoinjava.core.array;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Demonstrates converting a primitive {@code int[]} into a
 * {@code List<Integer>} using the Stream API, Guava's {@code Ints.asList},
 * Apache Commons Lang3, and plain iteration. The resulting lists are built
 * but not printed; run with a debugger or add print statements to inspect
 * them.
 */
public class ConvertPrimitiveArrayToList {

  public static void main(String[] args) {

    /*List<Integer> list = Arrays.asList(new Integer[]{1,2,3,4});	//Works!

    List<Integer> list1 = Arrays.asList(new int[]{1,2,3,4});	//Does not work!*/

    int[] intArray = new int[]{0, 1, 2, 3, 4, 5};

    //1 - Stream
    List<Integer> list1 = IntStream.of(intArray).boxed().toList();

    //2 - Guava
    List<Integer> list2 = Ints.asList(intArray);

    //3 - Commons Lang3
    List<Integer> list3 = Arrays.asList(ArrayUtils.toObject(intArray));

    //4 - Iteration
    List<Integer> list4 = new ArrayList<Integer>();
    for (int value : intArray) {
      list4.add(value);
    }

    //5
  }
}
