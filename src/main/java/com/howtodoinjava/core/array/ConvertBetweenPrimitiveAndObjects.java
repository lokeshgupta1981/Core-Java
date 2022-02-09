package com.howtodoinjava.core.array;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class ConvertBetweenPrimitiveAndObjects {
  public static void main(String[] args) {
    int[] primitiveArray = new int[]{0, 1, 2, 3, 4, 5};
    Integer[] objectArray = new Integer[]{0, 1, 2, 3, 4, 5};

    //1.Primitive array to object array
    Integer[] outputArray1 = Arrays.stream(primitiveArray)
        .boxed()
        .toArray(Integer[]::new);

    System.out.println(Arrays.toString(outputArray1));

    Integer[] outputArray2 = ArrayUtils.toObject(primitiveArray);

    System.out.println(Arrays.toString(outputArray2));

    //2. Object array to primitive array
    int[] outputArray3 = Arrays.stream(objectArray)
        .mapToInt(Integer::intValue)
        .toArray();

    System.out.println(Arrays.toString(outputArray3));

    int[] outputArray4 = ArrayUtils.toPrimitive(objectArray);

    System.out.println(Arrays.toString(outputArray4));
  }
}
