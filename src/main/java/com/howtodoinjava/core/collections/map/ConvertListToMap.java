package com.howtodoinjava.core.collections.map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.TransformerUtils;

public class ConvertListToMap {

  public static void main(String[] args) {
    // Creating unique employee data list
    List<Employee> uniqueEmployeeList = new ArrayList<>();

    // Adding employees to list
    uniqueEmployeeList.add(new Employee(1, "first employee"));
    uniqueEmployeeList.add(new Employee(2, "second employee"));
    uniqueEmployeeList.add(new Employee(3, "third employee"));
    uniqueEmployeeList.add(new Employee(4, "fourth employee"));
    uniqueEmployeeList.add(new Employee(5, "fifth employee"));

    // Creating duplicate employee data list
    List<Employee> duplicateEmployeeList = new ArrayList<>();

    // Adding employees to list
    duplicateEmployeeList.add(new Employee(1, "first employee"));
    duplicateEmployeeList.add(new Employee(2, "second employee"));
    duplicateEmployeeList.add(new Employee(1, "third employee"));
    duplicateEmployeeList.add(new Employee(4, "fourth employee"));
    duplicateEmployeeList.add(new Employee(2, "fifth employee"));

    //1
    Map<Integer, Employee> employeeMap = new HashMap<>();

    for (Employee employee : uniqueEmployeeList) {

      employeeMap.put(employee.id(), employee);
    }

    //2
    Map<Integer, List<Employee>> employeeMapWithListValue = new HashMap<>();

    for (Employee employee : duplicateEmployeeList) {
      if (employeeMapWithListValue.containsKey(employee.id())) {
        employeeMapWithListValue.get(employee.id()).add(employee);
      } else {
        ArrayList<Employee> list = new ArrayList<>();
        list.add(employee);
        employeeMapWithListValue.put(employee.id(), list);
      }
    }

    //3

    employeeMap = new HashMap<>();

    employeeMap = uniqueEmployeeList.stream()
        .collect(Collectors.toMap(Employee::id, Function.identity()));

    //4
    employeeMapWithListValue = new HashMap<>();

    employeeMapWithListValue = duplicateEmployeeList.stream()
        .collect(Collectors.toMap(item -> item.id(),
            item -> new ArrayList<>(Arrays.asList(item)),
            (l1, l2) -> {
              l1.addAll(l2);
              return l1;
            }));
    System.out.println(employeeMapWithListValue);

    //5

    employeeMapWithListValue = duplicateEmployeeList.stream()
        .collect(Collectors.groupingBy(Employee::id,
            Collectors.mapping(Function.identity(), Collectors.toList())));

    //6
    Map<Integer, Employee> employeeMap1 = new HashMap<>();
    MapUtils.populateMap(employeeMap1, uniqueEmployeeList, Employee::id);

    employeeMapWithListValue = new HashMap<>();

    Multimap employeeMultiMap = ArrayListMultimap.create();
    duplicateEmployeeList.stream().forEach(e -> employeeMultiMap.put(e.id(), e));

    System.out.println(employeeMultiMap);
  }


}

record Employee(int id, String name) {

}