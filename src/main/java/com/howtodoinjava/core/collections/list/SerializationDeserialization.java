package com.howtodoinjava.core.collections.list;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerializationDeserialization {

  public static void main(String[] args) {

    //1
    ArrayList<String> namesList = new ArrayList<>(List.of("alex", "brian", "charles"));

    namesList.add("alex");
    namesList.add("brian");
    namesList.add("charles");

    try (FileOutputStream fos = new FileOutputStream("listData");
        ObjectOutputStream oos = new ObjectOutputStream(fos);) {

      oos.writeObject(namesList);

    } catch (FileNotFoundException e) {
      log.error("File not found : ", e);
      throw new RuntimeException(e);
    } catch (IOException e) {
      log.error("Error while writing data : ", e);
      throw new RuntimeException(e);
    }

    //2

    ArrayList<Employee> employees = new ArrayList<>();

    employees.add(new Employee(1L, "lokesh", "gupta"));
    employees.add(new Employee(2L, "brian", "motto"));

    try (FileOutputStream fos = new FileOutputStream("employeeData");
        ObjectOutputStream oos = new ObjectOutputStream(fos);) {

      oos.writeObject(employees);

    } catch (FileNotFoundException e) {
      log.error("File not found : ", e);
      throw new RuntimeException(e);
    } catch (IOException ioe) {
      log.error("Error while writing data : ", ioe);
      ioe.printStackTrace();
    }

    //3
    ArrayList<String> list = null;

    try (FileInputStream fis = new FileInputStream("listData");
        ObjectInputStream ois = new ObjectInputStream(fis);) {

      list = (ArrayList) ois.readObject();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ClassNotFoundException c) {
      System.out.println("Class not found");
      c.printStackTrace();
    }

    //Verify list data
    for (String name : list) {
      System.out.println(name);
    }

    //4

    ArrayList<Employee> employeesList = null;

    try (FileInputStream fis = new FileInputStream("employeeData");
        ObjectInputStream ois = new ObjectInputStream(fis);) {

      employeesList = (ArrayList) ois.readObject();

    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ClassNotFoundException c) {
      System.out.println("Class not found");
      c.printStackTrace();
    }

    //Verify list data
    for (Employee employee : employeesList) {
      System.out.println(employee);
    }


  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Employee implements Serializable {

  private Long id;
  private String firstName;
  private String lastName;
}
