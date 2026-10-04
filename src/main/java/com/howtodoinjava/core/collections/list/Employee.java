package com.howtodoinjava.core.collections.list;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Serializable employee used by the ArrayList serialization example and by RemoveIf.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;
  private String firstName;
  private String lastName;
}
