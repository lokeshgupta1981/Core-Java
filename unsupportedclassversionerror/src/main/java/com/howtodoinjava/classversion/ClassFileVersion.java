package com.howtodoinjava.classversion;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public record ClassFileVersion(int major, int minor) {

  private static final int MAGIC = 0xCAFEBABE;

  public static ClassFileVersion of(Path classFile) throws IOException {
    try (InputStream in = Files.newInputStream(classFile)) {
      return of(in);
    }
  }

  public static ClassFileVersion of(InputStream in) throws IOException {
    DataInputStream data = new DataInputStream(in);
    if (data.readInt() != MAGIC) {
      throw new IOException("Not a class file");
    }
    int minor = data.readUnsignedShort();
    int major = data.readUnsignedShort();
    return new ClassFileVersion(major, minor);
  }

  public int javaVersion() {
    return major - 44;
  }

  public boolean usesPreviewFeatures() {
    return minor == 0xFFFF;
  }

  public static void main(String[] args) throws IOException {
    for (String arg : args) {
      ClassFileVersion version = of(Path.of(arg));
      System.out.println(arg + ": " + version.major() + "." + version.minor()
          + " (Java " + version.javaVersion() + ")");
    }
  }
}
