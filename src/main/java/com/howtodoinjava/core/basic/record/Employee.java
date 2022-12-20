package com.howtodoinjava.core.basic.record;

import java.io.Serializable;
import java.util.Objects;

public record Employee(Long id, String firstName, String lastName, String email, int age)
    implements Serializable {

  static boolean minor;

  public boolean isMinor() {
    return minor;
  }

  public String fullName() {
    return firstName + " " + lastName;
  }

  /*public Employee {
    if (age < 18) {
      //minor = true;
      throw new IllegalArgumentException("You cannot hire a minor person as employee");
    }
  }
*/
  public Employee {
    Objects.requireNonNull(id);
    Objects.requireNonNull(email);

    if (age < 18) {
      throw new IllegalArgumentException("You cannot hire a minor person as employee");
    }
  }

}
