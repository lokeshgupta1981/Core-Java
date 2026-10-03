package com.howtodoinjava.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;

/**
 * Small helpers that serialize an object to a byte array and read it back.
 */
public final class SerializationUtil {

  private SerializationUtil() {
  }

  public static byte[] toBytes(Object object) {
    var bytes = new ByteArrayOutputStream();
    try (var out = new ObjectOutputStream(bytes)) {
      out.writeObject(object);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return bytes.toByteArray();
  }

  public static Object fromBytes(byte[] data) throws IOException, ClassNotFoundException {
    return fromBytes(data, null);
  }

  public static Object fromBytes(byte[] data, ObjectInputFilter filter)
      throws IOException, ClassNotFoundException {
    try (var in = new ObjectInputStream(new ByteArrayInputStream(data))) {
      if (filter != null) {
        in.setObjectInputFilter(filter);
      }
      return in.readObject();
    }
  }

  /** Replaces the last occurrence of a 4-byte int in the stream (used to simulate tampering). */
  public static byte[] replaceInt(byte[] data, int oldValue, int newValue) {
    byte[] copy = data.clone();
    for (int i = copy.length - 4; i >= 0; i--) {
      if (readInt(copy, i) == oldValue) {
        writeInt(copy, i, newValue);
        return copy;
      }
    }
    throw new IllegalArgumentException("value not found: " + oldValue);
  }

  /** Replaces the first occurrence of an 8-byte long in the stream (used for serialVersionUID). */
  public static byte[] replaceLong(byte[] data, long oldValue, long newValue) {
    byte[] copy = data.clone();
    for (int i = 0; i <= copy.length - 8; i++) {
      long value = ((long) readInt(copy, i) << 32) | (readInt(copy, i + 4) & 0xFFFFFFFFL);
      if (value == oldValue) {
        writeInt(copy, i, (int) (newValue >>> 32));
        writeInt(copy, i + 4, (int) newValue);
        return copy;
      }
    }
    throw new IllegalArgumentException("value not found: " + oldValue);
  }

  private static int readInt(byte[] b, int i) {
    return ((b[i] & 0xFF) << 24) | ((b[i + 1] & 0xFF) << 16) | ((b[i + 2] & 0xFF) << 8) | (b[i + 3] & 0xFF);
  }

  private static void writeInt(byte[] b, int i, int v) {
    b[i] = (byte) (v >>> 24);
    b[i + 1] = (byte) (v >>> 16);
    b[i + 2] = (byte) (v >>> 8);
    b[i + 3] = (byte) v;
  }
}
