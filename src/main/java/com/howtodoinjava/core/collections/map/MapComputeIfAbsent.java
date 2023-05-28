package com.howtodoinjava.core.collections.map;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapComputeIfAbsent {

  public static void main(String[] args) {
    Map<Integer, BigInteger> map = new HashMap<>();

    map.computeIfAbsent(5, MapComputeIfAbsent::factorial);
    map.computeIfAbsent(6, MapComputeIfAbsent::factorial);

    //Inserting in another place
    map.computeIfAbsent(6, MapComputeIfAbsent::factorial);  //Factorial is not calculated here

    //Null
    map.computeIfAbsent(7, k -> null);
    assertNull(map.get(7));

    //Exception
    try {
      map.computeIfAbsent(7, k -> {
        throw new RuntimeException();
      });
    } catch (Exception e) {
      assertNull(map.get(7));
    }

    //Multi-value Map
    Map<String, List<String>> multiMap = new HashMap<>();

    multiMap.computeIfAbsent("State1", k -> new ArrayList<>()).add("City1");
    multiMap.computeIfAbsent("State1", k -> new ArrayList<>()).add("City2");

    System.out.println(multiMap); //{State1=[City1, City2]}

    //Difference with other compute functions
    map = new HashMap<>();
    map.put(1, null);

    map.computeIfPresent(1, (k, v) -> MapComputeIfAbsent.factorial(k));
  }

  static BigInteger factorial(Integer num) {
    System.out.println("Calculating the factorial of : " + num);
    return BigIntegerMath.factorial(num);
  }
}
