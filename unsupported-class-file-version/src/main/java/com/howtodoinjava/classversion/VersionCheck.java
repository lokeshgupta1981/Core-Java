package com.howtodoinjava.classversion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Prints the class file version of .class and .jar files and the highest
 * version the running JVM accepts.
 *
 * Run: java -cp target/classes com.howtodoinjava.classversion.VersionCheck target/classes/com/howtodoinjava/classversion/Hello.class
 */
public class VersionCheck {

  public static void main(String[] args) throws IOException {
    int supported = ClassFileVersion.highestSupportedByThisJvm();
    System.out.println("Running on Java " + Runtime.version().feature()
        + ", accepts class files up to major version " + supported);

    for (String arg : args) {
      Path path = Path.of(arg);
      if (!Files.isRegularFile(path)) {
        System.out.println(arg + ": file not found");
        continue;
      }
      if (arg.endsWith(".jar")) {
        Map<String, ClassFileVersion> versions = ClassFileVersion.readJar(path);
        ClassFileVersion.newest(versions).ifPresentOrElse(
            e -> System.out.println(arg + ": " + versions.size() + " classes, newest "
                + e.getValue() + " in " + e.getKey()),
            () -> System.out.println(arg + ": no classes"));
      } else {
        ClassFileVersion version = ClassFileVersion.read(path);
        String verdict = version.major() <= supported ? "OK" : "too new for this JVM";
        System.out.println(arg + ": " + version + " -> " + verdict);
      }
    }
  }
}
