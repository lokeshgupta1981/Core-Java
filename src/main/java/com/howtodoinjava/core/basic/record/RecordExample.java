package com.howtodoinjava.core.basic.record;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RecordExample {

  public static void main(String[] args) {
    Employee e = new Employee(1l, "Lokesh", "Gupta", "howtodoinjava@gmail.com", 38);

    System.out.println(e.id());
    System.out.println(e.email());
    System.out.println(e);

    /*writeToFile(e, "employee1");
    System.out.println(readFromFile("employee1"));*/

    Employee employee = new Employee(1l, "Amit", "Gupta", "email@domain.com", 17);
    System.out.println(employee.isMinor());
    System.out.println(employee.fullName());
  }

  static void writeToFile(Employee obj, String path) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
      oos.writeObject(obj);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static Employee readFromFile(String path) {
    Employee result = null;
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
      result = (Employee) ois.readObject();
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}