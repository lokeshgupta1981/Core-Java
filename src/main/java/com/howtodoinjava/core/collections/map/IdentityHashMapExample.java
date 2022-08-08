package com.howtodoinjava.core.collections.map;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

public class IdentityHashMapExample {
  public static void main(String[] args) {
    // 2 similar keys but different instances in memory
    Integer key1 = new Integer(10);
    Integer key2 = new Integer(10);

    // Creating IdentityHashMap
    IdentityHashMap<Integer, String> identityHashMap = new IdentityHashMap<>();

    // Putting values in IdentityHashMap
    identityHashMap.put(key1, "India");
    identityHashMap.put(key2, "USA");
    System.out.println("Identity HashMap : " + identityHashMap);

    // Creating HashMap
    HashMap<Integer, String> hashMap = new HashMap<>();

    // Putting values in HashMap
    hashMap.put(key1, "India");
    hashMap.put(key2, "USA");
    System.out.println("HashMap : " + hashMap);

    //2

    // Creating Vehicle Objects
    Vehicle vehicle = new Vehicle("Honda", 2015);

    // Creating IdentityHashMap
    Map<Vehicle, String> identityHashMap1 = new IdentityHashMap<>();
    identityHashMap1.put(vehicle, "Old Vehicle");

    // Changing key state
    vehicle.setName("Modified Vehicle");
    vehicle.setYear(2022);

    // Getting value for key vehicle from the map
    System.out.println(identityHashMap1.get(vehicle));
  }
}

class Vehicle {

  private String name;
  private int year;

  public Vehicle(String name, int year) {
    this.name = name;
    this.year = year;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  @Override
  public String toString() {
    return "Vehicle{" +
        "vehicleName='" + name + '\'' +
        ", modelYear=" + year +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Vehicle vehicle = (Vehicle) o;

    if (Objects.equals(year, vehicle.year)) return false;
    return Objects.equals(name, vehicle.name);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + year;
    return result;
  }
}
