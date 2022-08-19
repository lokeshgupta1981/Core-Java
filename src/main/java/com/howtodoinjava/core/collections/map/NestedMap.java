package com.howtodoinjava.core.collections.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NestedMap {
  public static void main(String[] args) {
    Map<String, Map<String, String>> employeeMap = new HashMap<>();

    Map<String, String> addressMap = new HashMap<>();
    addressMap.put("Permanent", "Florida");
    addressMap.put("Postal", "Canada");

    employeeMap.put("Alex", addressMap);

    System.out.println(employeeMap);

    employeeMap.get("Alex").put("Hideout", "UAE");
    System.out.println(employeeMap);

    employeeMap.get("Alex").remove("Hideout");
    System.out.println(employeeMap);

    // Iterate OuterMap
    for (Map.Entry<String, Map<String, String>> empMap : employeeMap.entrySet()) {
      Map<String, String> addMap = empMap.getValue();

      // Iterate InnerMap
      for (Map.Entry<String, String> addressSet : addMap.entrySet()) {
        System.out.println(addressSet.getKey() + " :: " + addressSet.getValue());
      }
    }
    //2

    // Creating Nested Map
    List<Student> studentList = List.of(
        new Student(1, "A", "Course1"),
        new Student(2, "B", "Course2"),
        new Student(3, "C", "Course3"));

    Map<Integer, Map<String, String>> studentNestedMap =
        studentList.stream()
            .collect(Collectors.groupingBy(s -> s.getStudentId(),
                Collectors.toMap(Student::getStudentName, Student::getCourse)));

    System.out.println(studentNestedMap);
  }
}

class Student {
  private Integer studentId;
  private String studentName;
  private String course;

  public Student(Integer studentId, String studentName, String course) {
    this.studentId = studentId;
    this.studentName = studentName;
    this.course = course;
  }

  public Integer getStudentId() {
    return studentId;
  }

  public void setStudentId(Integer studentId) {
    this.studentId = studentId;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }
}