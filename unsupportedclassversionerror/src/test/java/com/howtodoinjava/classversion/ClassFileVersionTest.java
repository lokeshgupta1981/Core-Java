package com.howtodoinjava.classversion;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class ClassFileVersionTest {

  @TempDir
  Path out;

  private static final Path HELLO = Path.of("com/howtodoinjava/Hello.class");

  @Test
  void whenCompiledWithoutRelease_thenMajorVersionMatchesTheCompilerJdk() throws Exception {
    Jdks.javac(out, "hello");

    ClassFileVersion version = ClassFileVersion.of(out.resolve(HELLO));

    assertThat(version.major()).isEqualTo(69);
    assertThat(version.minor()).isZero();
    assertThat(version.javaVersion()).isEqualTo(25);
  }

  @Test
  void whenCompiledWithEachRelease_thenMajorVersionIsReleasePlus44() throws Exception {
    for (int release = 10; release <= 25; release++) {
      Path dir = out.resolve("r" + release);
      Jdks.javac(dir, "hello", "--release", String.valueOf(release));

      assertThat(ClassFileVersion.of(dir.resolve(HELLO)).major()).isEqualTo(release + 44);
    }
  }

  @Test
  void whenCompiledWithPreviewFeatures_thenMinorVersionIs65535() throws Exception {
    Jdks.javac(out, "preview", "--release", "25", "--enable-preview");

    ClassFileVersion version = ClassFileVersion.of(out.resolve("com/howtodoinjava/Preview.class"));

    assertThat(version.major()).isEqualTo(69);
    assertThat(version.minor()).isEqualTo(65535);
    assertThat(version.usesPreviewFeatures()).isTrue();
  }

  @Test
  void whenRunningJavap_thenItPrintsTheVersion() throws Exception {
    Jdks.javac(out, "hello");
    Path jdk25 = Path.of(System.getProperty("java.home"));

    ProcessBuilder builder = new ProcessBuilder(Jdks.tool(jdk25, "javap").toString(), "-v",
        "-cp", out.toString(), "com.howtodoinjava.Hello").redirectErrorStream(true);
    builder.environment().remove("JAVA_TOOL_OPTIONS");
    String output = new String(builder.start().getInputStream().readAllBytes());

    assertThat(output).contains("minor version: 0").contains("major version: 69");
  }
}
