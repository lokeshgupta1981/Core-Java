package com.howtodoinjava.core.array;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConcatArrays {
  public static void main(String[] args) {
    String[] strArray1 = {"1", "2", "3"};
    String[] strArray2 = {"4", "5", "6"};

    int[] intArray1 = {1, 2, 3};
    int[] intArray2 = {4, 5, 6};

    String[] resultObj;
    int[] result;

    //Apache Commons Lang
    ArrayUtils.addAll(strArray1, strArray2);
    result = ArrayUtils.addAll(intArray1, intArray2);
    System.out.println(Arrays.toString(result));

    //Guava
    resultObj = ObjectArrays.concat(strArray1, strArray2, String.class);
    result = Ints.concat(intArray1, intArray2);
    System.out.println(resultObj);
    System.out.println(result);

    //1
    result = new int[intArray1.length + intArray2.length];
    int index = 0;

    for (int item: intArray1) {
      result[index++] = item;
    }
    for (int item: intArray2) {
      result[index++] = item;
    }

    System.out.println(Arrays.toString(result));

    //2
    result = new int[intArray1.length + intArray2.length];

    System.arraycopy(intArray1, 0, result, 0, intArray1.length);
    System.arraycopy(intArray2, 0, result, intArray1.length, intArray2.length);

    //3
    result = IntStream.concat(Arrays.stream(intArray1),
        Arrays.stream(intArray2)).toArray();

    String[] both = Stream.concat(Arrays.stream(strArray1),
            Arrays.stream(strArray2))
        .toArray(String[]::new);
    System.out.println(both);

    resultObj = concatWithArrayCopy(strArray1, strArray2);
    System.out.println(Arrays.toString(resultObj));

  }

  static <T> T[] concatWithStream(T[] array1, T[] array2) {
    return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
        .toArray(size -> (T[]) Array.newInstance(array1.getClass().getComponentType(), size));
  }

  static <T> T[] concatWithCollection(T[] array1, T[] array2) {
    List<T> resultList = new ArrayList<>(array1.length + array2.length);
    Collections.addAll(resultList, array1);
    Collections.addAll(resultList, array2);
    T[] resultArray = (T[]) Array.newInstance(array1.getClass().getComponentType(), 0);
    return resultList.toArray(resultArray);
  }

  static <T> T[] concatWithArrayCopy(T[] array1, T[] array2) {
    T[] result = Arrays.copyOf(array1, array1.length + array2.length);
    System.arraycopy(array2, 0, result, array1.length, array2.length);
    return result;
  }
}
