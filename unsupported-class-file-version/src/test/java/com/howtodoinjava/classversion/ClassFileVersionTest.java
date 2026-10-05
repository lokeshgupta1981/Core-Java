package com.howtodoinjava.classversion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ClassFileFormatVersion;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ClassFileVersionTest {

  private static final Path CLASSES = Path.of(System.getProperty("classes.dir", "target/classes"));

  @Test
  void ourClassesHaveMajorVersion69() throws IOException {
    Path helloClass = CLASSES.resolve("com/howtodoinjava/classversion/Hello.class");

    ClassFileVersion version = ClassFileVersion.read(helloClass);

    assertThat(version.major()).isEqualTo(69);
    assertThat(version.minor()).isZero();
    assertThat(version.javaVersion()).isEqualTo("25");
    assertThat(version).hasToString("69.0 (Java 25)");
  }

  @Test
  void headerBytesAreCafeBabeThenMinorThenMajor() {
    byte[] header = {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE, 0x00, 0x00, 0x00, 0x45};

    ClassFileVersion version = readQuietly(new ByteArrayInputStream(header));

    assertThat(version.major()).isEqualTo(0x45).isEqualTo(69);
  }

  @Test
  void rejectsFilesThatAreNotClassFiles() {
    byte[] notAClass = "apple".getBytes();

    assertThatThrownBy(() -> ClassFileVersion.read(new ByteArrayInputStream(notAClass)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageStartingWith("Not a class file");
  }

  @Test
  void jdkClassesMatchTheRunningJvm() throws IOException {
    // String.class comes from the running JDK, so its version is the JVM's own version
    try (InputStream in = String.class.getResourceAsStream("String.class")) {
      ClassFileVersion version = ClassFileVersion.read(in);
      assertThat(version.major()).isEqualTo(ClassFileVersion.highestSupportedByThisJvm());
      assertThat(version.major()).isEqualTo(Runtime.version().feature() + 44);
    }
  }

  @ParameterizedTest(name = "Java {0} -> major {1}")
  @CsvSource({
      "8, 52", "9, 53", "10, 54", "11, 55", "12, 56", "13, 57", "14, 58", "15, 59",
      "16, 60", "17, 61", "18, 62", "19, 63", "20, 64", "21, 65", "22, 66",
      "23, 67", "24, 68", "25, 69"
  })
  void javaReleaseToMajorVersionTable(int release, int major) {
    // ClassFileFormatVersion (Java 20+) is the JDK's own table of class file versions
    ClassFileFormatVersion format = ClassFileFormatVersion.valueOf("RELEASE_" + release);

    assertThat(format.major()).isEqualTo(major);
    assertThat(new ClassFileVersion(major, 0).javaVersion()).isEqualTo(String.valueOf(release));
  }

  private static ClassFileVersion readQuietly(InputStream in) {
    try {
      return ClassFileVersion.read(in);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
