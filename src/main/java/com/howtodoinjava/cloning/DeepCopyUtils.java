package com.howtodoinjava.cloning;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Deep copy of a Serializable object graph through an in-memory byte array.
 */
public final class DeepCopyUtils {

  private DeepCopyUtils() {
  }

  @SuppressWarnings("unchecked")
  public static <T extends Serializable> T deepCopy(T object) {
    try {
      var bytes = new ByteArrayOutputStream();
      try (var out = new ObjectOutputStream(bytes)) {
        out.writeObject(object);
      }
      try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
        return (T) in.readObject();
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new IllegalStateException("Deep copy failed", e);
    }
  }
}
