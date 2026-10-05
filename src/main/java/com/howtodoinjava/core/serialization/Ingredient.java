package com.howtodoinjava.core.serialization;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;

/**
 * An Externalizable class: the class itself writes and reads every field, and Java calls
 * the public no-arg constructor before readExternal().
 */
public class Ingredient implements Externalizable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String name;
  private int grams;

  public Ingredient() {                     // required by Externalizable
  }

  public Ingredient(String name, int grams) {
    this.name = name;
    this.grams = grams;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeUTF(name);
    out.writeInt(grams);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException {
    name = in.readUTF();                    // same order as writeExternal()
    grams = in.readInt();
  }

  @Override
  public String toString() {
    return "Ingredient[name=" + name + ", grams=" + grams + "]";
  }
}
