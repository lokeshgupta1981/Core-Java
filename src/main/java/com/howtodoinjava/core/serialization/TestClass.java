package com.howtodoinjava.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestClass implements Serializable {

  private static final long serialVersionUID = 1L;

  private String firstName;
  private String lastName ;
  private List permissions;

  public TestClass deepCopy() throws Exception {
    //Serialization of object
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(this);

    //De-serialization of object
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream in = new ObjectInputStream(bis);
    TestClass copied = (TestClass) in.readObject();
    return copied;
  }
}
