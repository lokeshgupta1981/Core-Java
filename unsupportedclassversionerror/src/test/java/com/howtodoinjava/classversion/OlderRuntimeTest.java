package com.howtodoinjava.classversion;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class OlderRuntimeTest {

  @TempDir
  Path out;

  @Test
  void givenClassCompiledWithJdk25_whenRunOnJava21_thenUnsupportedClassVersionError() throws Exception {
    Path jdk21 = Jdks.home(21);
    Jdks.javac(out, "hello");

    Jdks.Result run = Jdks.java(jdk21, "-cp", out.toString(), "com.howtodoinjava.Hello");

    assertThat(run.exitCode()).isEqualTo(1);
    assertThat(run.output())
        .contains("Error: LinkageError occurred while loading main class com.howtodoinjava.Hello")
        .contains("java.lang.UnsupportedClassVersionError: com/howtodoinjava/Hello has been compiled by a more "
            + "recent version of the Java Runtime (class file version 69.0), this version of the Java Runtime "
            + "only recognizes class file versions up to 65.0");
  }

  @Test
  void givenClassCompiledWithRelease21_whenRunOnJava21_thenItRuns() throws Exception {
    Path jdk21 = Jdks.home(21);
    Jdks.javac(out, "hello", "--release", "21");

    Jdks.Result run = Jdks.java(jdk21, "-cp", out.toString(), "com.howtodoinjava.Hello");

    assertThat(run.exitCode()).isZero();
    assertThat(run.output()).isEqualToIgnoringNewLines("Hello from Java 21");
  }

  @Test
  void givenClassCompiledWithJdk25_whenRunOnOlderJdks_thenMessageNamesTheirMaximum() throws Exception {
    Jdks.javac(out, "hello");

    for (int feature : new int[] {17, 11, 8}) {
      Path jdk = Jdks.home(feature);
      Jdks.Result run = Jdks.java(jdk, "-cp", out.toString(), "com.howtodoinjava.Hello");

      assertThat(run.output()).contains("only recognizes class file versions up to " + (feature + 44) + ".0");
    }
  }
}
