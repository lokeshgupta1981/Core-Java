package com.howtodoinjava.classversion;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The version stored in the first 8 bytes of a .class file.
 * Layout: magic 0xCAFEBABE (4 bytes), minor_version (2 bytes), major_version (2 bytes).
 */
public record ClassFileVersion(int major, int minor) {

  private static final int MAGIC = 0xCAFEBABE;

  /** Reads the version from the header of one class file. */
  public static ClassFileVersion read(InputStream in) throws IOException {
    DataInputStream data = new DataInputStream(in);
    int magic = data.readInt();
    if (magic != MAGIC) {
      throw new IllegalArgumentException("Not a class file, magic is 0x" + Integer.toHexString(magic));
    }
    int minor = data.readUnsignedShort();
    int major = data.readUnsignedShort();
    return new ClassFileVersion(major, minor);
  }

  public static ClassFileVersion read(Path classFile) throws IOException {
    try (InputStream in = Files.newInputStream(classFile)) {
      return read(in);
    }
  }

  /**
   * Returns the highest class file version per entry in a JAR, ignoring
   * META-INF/versions/** (multi-release JARs may keep newer classes there on purpose).
   */
  public static Map<String, ClassFileVersion> readJar(Path jar) throws IOException {
    Map<String, ClassFileVersion> versions = new TreeMap<>();
    try (JarFile jarFile = new JarFile(jar.toFile())) {
      for (JarEntry entry : jarFile.stream().toList()) {
        String name = entry.getName();
        if (name.endsWith(".class") && !name.startsWith("META-INF/versions/")) {
          try (InputStream in = jarFile.getInputStream(entry)) {
            versions.put(name, read(in));
          }
        }
      }
    }
    return versions;
  }

  /** The entry that needs the newest Java runtime. */
  public static Optional<Map.Entry<String, ClassFileVersion>> newest(Map<String, ClassFileVersion> versions) {
    return versions.entrySet().stream()
        .max(Comparator.comparingInt(e -> e.getValue().major()));
  }

  /** Java release that produces this major version: 52 is Java 8, 69 is Java 25. */
  public String javaVersion() {
    if (major >= 49) {
      return String.valueOf(major - 44);        // Java 5 and later
    }
    return "1." + (major - 44);                 // 45 = 1.1, 48 = 1.4
  }

  /** Highest major version the running JVM can load, e.g. 65 on JDK 21 and 69 on JDK 25. */
  public static int highestSupportedByThisJvm() {
    return java.lang.reflect.ClassFileFormatVersion.latest().major();
  }

  @Override
  public String toString() {
    return major + "." + minor + " (Java " + javaVersion() + ")";
  }
}
