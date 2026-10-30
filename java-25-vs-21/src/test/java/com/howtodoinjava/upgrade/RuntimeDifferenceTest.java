package com.howtodoinjava.upgrade;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Runs the same class files on JDK 21 and JDK 25. Set JAVA21_HOME to run the JDK 21 side.
 */
class RuntimeDifferenceTest {

  @TempDir
  static Path classesDir;

  static Path classes;

  @BeforeAll
  static void compileDemos() throws Exception {
    classes = Jvm.compileDemosForJava21(classesDir);
  }

  @Test
  void givenSynchronizedSleep_whenRunOnJava25_thenVirtualThreadsAreNotPinned() throws Exception {
    Jvm.Result java25 = Jvm.run(Jvm.JDK_25, classes, "PinningDemo",
        "-Djdk.virtualThreadScheduler.parallelism=4");

    assertThat(Jvm.elapsedMillis(java25.output())).isLessThan(1_000);
  }

  @Test
  void givenSynchronizedSleep_whenRunOnJava21_thenFourCarriersRunTasksInTurns() throws Exception {
    assumeTrue(Jvm.JDK_21 != null, "JAVA21_HOME not set");

    Jvm.Result java21 = Jvm.run(Jvm.JDK_21, classes, "PinningDemo",
        "-Djdk.virtualThreadScheduler.parallelism=4");

    assertThat(Jvm.elapsedMillis(java21.output())).isGreaterThanOrEqualTo(2_500);
  }

  @Test
  void givenCompactObjectHeaders_whenEnabledOnJava25_thenPointTakes16Bytes() throws Exception {
    Jvm.Result standard = Jvm.run(Jvm.JDK_25, classes, "HeaderFootprint",
        "-Xmx1g", "-XX:+UseSerialGC");
    Jvm.Result compact = Jvm.run(Jvm.JDK_25, classes, "HeaderFootprint",
        "-Xmx1g", "-XX:+UseSerialGC", "-XX:+UseCompactObjectHeaders");

    assertThat(standard.output()).contains("bytes per Point = 24");
    assertThat(compact.output()).contains("bytes per Point = 16");
  }

  @Test
  void givenCompactObjectHeadersFlag_whenRunOnJava21_thenJvmDoesNotStart() throws Exception {
    assumeTrue(Jvm.JDK_21 != null, "JAVA21_HOME not set");

    Jvm.Result java21 = Jvm.run(Jvm.JDK_21, classes, "Started", "-XX:+UseCompactObjectHeaders");

    assertThat(java21.exitCode()).isNotZero();
    assertThat(java21.output()).contains("Unrecognized VM option 'UseCompactObjectHeaders'");
  }

  @Test
  void givenUnsafeMemoryAccess_whenRunOnJava25_thenWarningIsPrinted() throws Exception {
    Jvm.Result java25 = Jvm.run(Jvm.JDK_25, classes, "UnsafeDemo");

    assertThat(java25.output())
        .contains("WARNING: A terminally deprecated method in sun.misc.Unsafe has been called")
        .contains("value = 42");
  }

  @Test
  void givenUnsafeMemoryAccess_whenRunOnJava21_thenNoWarning() throws Exception {
    assumeTrue(Jvm.JDK_21 != null, "JAVA21_HOME not set");

    Jvm.Result java21 = Jvm.run(Jvm.JDK_21, classes, "UnsafeDemo");

    assertThat(java21.output()).doesNotContain("WARNING").contains("value = 42");
  }

  @Test
  void givenSecurityManagerOption_whenRunOnJava25_thenJvmFailsToStart() throws Exception {
    Jvm.Result java25 = Jvm.run(Jvm.JDK_25, classes, "Started", "-Djava.security.manager=allow");

    assertThat(java25.exitCode()).isNotZero();
    assertThat(java25.output()).contains("Enabling a Security Manager is not supported");
  }

  @Test
  void givenSecurityManagerOption_whenRunOnJava21_thenApplicationStarts() throws Exception {
    assumeTrue(Jvm.JDK_21 != null, "JAVA21_HOME not set");

    Jvm.Result java21 = Jvm.run(Jvm.JDK_21, classes, "Started", "-Djava.security.manager=allow");

    assertThat(java21.exitCode()).isZero();
    assertThat(java21.output()).contains("started");
  }
}
