package com.howtodoinjava.core.objectToMap;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
  private Integer id;
  private String name;
  private LocalDate dateOfBirth;
  private List<String> locations;
  private List<Role> roles;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Role {
  private Integer id;
  private String name;
}


