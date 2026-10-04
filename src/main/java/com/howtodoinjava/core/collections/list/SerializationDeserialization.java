package com.howtodoinjava.core.collections.list;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Serializes and deserializes an ArrayList with ObjectOutputStream and ObjectInputStream:
 * to a file, to a byte array, with a checked cast, with other List classes and with an
 * ObjectInputFilter. Every printed line matches a result shown in the article.
 */
public class SerializationDeserialization {

  public static void main(String[] args) throws Exception {

    // 1. Serialize an ArrayList of strings to a file
    ArrayList<String> namesList = new ArrayList<>(List.of("alex", "brian", "charles"));
    try (FileOutputStream fos = new FileOutputStream("listData");
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(namesList);
    }
    System.out.println("listData bytes: " + Files.size(Path.of("listData")));

    // 2. Serialize an ArrayList of Employee objects to a file
    ArrayList<Employee> employees = new ArrayList<>();
    employees.add(new Employee(1L, "lokesh", "gupta"));
    employees.add(new Employee(2L, "brian", "motto"));
    try (FileOutputStream fos = new FileOutputStream("employeeData");
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(employees);
    }
    System.out.println("employeeData bytes: " + Files.size(Path.of("employeeData")));

    // 3. Deserialize the list of strings
    List<String> names;
    try (FileInputStream fis = new FileInputStream("listData");
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      @SuppressWarnings("unchecked")
      List<String> read = (List<String>) ois.readObject();
      names = read;
    }
    System.out.println("names: " + names);
    System.out.println("names class: " + names.getClass().getName());
    System.out.println("equals original: " + names.equals(namesList));
    System.out.println("same object: " + (names == namesList));

    // 4. Deserialize the list of employees
    List<Employee> employeeList;
    try (FileInputStream fis = new FileInputStream("employeeData");
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      @SuppressWarnings("unchecked")
      List<Employee> read = (List<Employee>) ois.readObject();
      employeeList = read;
    }
    for (Employee employee : employeeList) {
      System.out.println(employee);
    }
    System.out.println("employees equal: " + employeeList.equals(employees));
    System.out.println("employees same object: " + (employeeList == employees));

    // 5. An element that is not Serializable
    List<Contractor> contractors = new ArrayList<>(List.of(new Contractor("alex")));
    try {
      toBytes(contractors);
    } catch (NotSerializableException e) {
      System.out.println("5: " + e);
    }

    // 6. The unchecked cast fails late: a List<String> read as List<Employee>
    @SuppressWarnings("unchecked")
    List<Employee> wrong = (List<Employee>) fromBytes(toBytes(namesList));
    System.out.println("6: wrong.size() = " + wrong.size());
    try {
      Employee first = wrong.get(0);
      System.out.println(first);
    } catch (ClassCastException e) {
      System.out.println("6: " + e);
    }

    // 7. Checked alternative: fails at once with a clear message
    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(toBytes(employees)))) {
      List<Employee> checked = readList(ois, Employee.class);
      System.out.println("7: " + checked);
    }
    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(toBytes(namesList)))) {
      readList(ois, Employee.class);
    } catch (ClassCastException e) {
      System.out.println("7: " + e);
    }

    // 8. Byte array for a cache; capacity is not written
    byte[] bytes = toBytes(employees);
    System.out.println("8: employees bytes = " + bytes.length);
    ArrayList<Employee> bigCapacity = new ArrayList<>(1000);
    bigCapacity.addAll(employees);
    System.out.println("8: capacity 1000 bytes = " + toBytes(bigCapacity).length);
    @SuppressWarnings("unchecked")
    List<Employee> fromCache = (List<Employee>) fromBytes(bytes);
    System.out.println("8: " + fromCache);

    // 9. Other List classes after a round trip
    Map<String, List<String>> lists = new LinkedHashMap<>();
    lists.put("new ArrayList<>(...)", new ArrayList<>(List.of("alex", "brian")));
    lists.put("List.of(...)", List.of("alex", "brian"));
    lists.put("List.of(a, b, c)", List.of("alex", "brian", "charles"));
    lists.put("stream().toList()", namesList.stream().toList());
    lists.put("Arrays.asList(...)", Arrays.asList("alex", "brian"));
    lists.put("Collections.unmodifiableList(...)",
        Collections.unmodifiableList(new ArrayList<>(List.of("alex", "brian"))));
    lists.put("Collections.synchronizedList(...)",
        Collections.synchronizedList(new ArrayList<>(List.of("alex", "brian"))));
    lists.put("new LinkedList<>(...)", new LinkedList<>(List.of("alex", "brian")));
    lists.put("new CopyOnWriteArrayList<>(...)", new CopyOnWriteArrayList<>(List.of("alex", "brian")));
    for (Map.Entry<String, List<String>> e : lists.entrySet()) {
      Object copy = fromBytes(toBytes(e.getValue()));
      String modifiable;
      try {
        @SuppressWarnings("unchecked")
        List<String> l = (List<String>) copy;
        l.set(0, "zed");
        modifiable = "set() works";
      } catch (UnsupportedOperationException ex) {
        modifiable = "set() throws UnsupportedOperationException";
      }
      System.out.println("9: " + e.getKey() + " | " + e.getValue().getClass().getName()
          + " -> " + copy.getClass().getName() + " | " + modifiable);
    }
    try {
      toBytes(namesList.subList(0, 2));
    } catch (NotSerializableException e) {
      System.out.println("9: subList -> " + e);
    }
    @SuppressWarnings("unchecked")
    List<String> list12 = (List<String>) fromBytes(toBytes(List.of("alex", "brian")));
    List<String> editable = new ArrayList<>(list12);
    editable.set(0, "zed");
    System.out.println("9: editable copy -> " + editable);
    @SuppressWarnings("unchecked")
    List<String> asList = (List<String>) fromBytes(toBytes(Arrays.asList("alex", "brian")));
    try {
      asList.add("charles");
    } catch (UnsupportedOperationException e) {
      System.out.println("9: Arrays.asList copy add() -> " + e);
    }

    // Order, duplicates and null elements survive the round trip
    List<String> withDuplicates = new ArrayList<>(Arrays.asList("brian", "alex", "brian", null));
    System.out.println("9: duplicates -> " + fromBytes(toBytes(withDuplicates)));
    System.out.println("9: subList copy -> " + fromBytes(toBytes(new ArrayList<>(namesList.subList(0, 2)))));

    // 10. ObjectInputFilter: allow only the classes we expect
    ObjectInputFilter filter = ObjectInputFilter.Config.createFilter(
        "java.util.ArrayList;java.lang.*;com.howtodoinjava.core.collections.list.Employee;maxdepth=5;!*");
    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(toBytes(employees)))) {
      ois.setObjectInputFilter(filter);
      System.out.println("10: allowed -> " + ois.readObject());
    }
    try (ObjectInputStream ois = new ObjectInputStream(
        new ByteArrayInputStream(toBytes(new LinkedList<>(employees))))) {
      ois.setObjectInputFilter(filter);
      ois.readObject();
    } catch (InvalidClassException e) {
      System.out.println("10: rejected -> " + e);
    }

    Files.deleteIfExists(Path.of("listData"));
    Files.deleteIfExists(Path.of("employeeData"));
  }

  /** Writes any serializable object to a byte array. */
  static byte[] toBytes(Object obj) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(obj);
    }
    return bos.toByteArray();
  }

  /** Reads one object from a byte array. */
  static Object fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
      return ois.readObject();
    }
  }

  /** Reads a list and checks the type of every element, so no unchecked cast is needed. */
  static <T> List<T> readList(ObjectInputStream ois, Class<T> type)
      throws IOException, ClassNotFoundException {

    if (!(ois.readObject() instanceof List<?> raw)) {
      throw new ClassCastException("Expected a List");
    }
    List<T> result = new ArrayList<>(raw.size());
    for (Object item : raw) {
      result.add(type.cast(item));
    }
    return result;
  }
}

/** Deliberately not Serializable. */
class Contractor {

  private final String name;

  Contractor(String name) {
    this.name = name;
  }
}
