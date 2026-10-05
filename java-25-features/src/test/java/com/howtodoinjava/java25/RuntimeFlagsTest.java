package com.howtodoinjava.java25;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

/** Starts child JVMs with the new JDK 25 runtime flags. */
class RuntimeFlagsTest {

  private static final String PKG = "com.howtodoinjava.java25.runtime.";

  @TempDir
  Path tmp;

  @Test
  void compactObjectHeadersShrinkSmallObjects() throws Exception {
    String regular = Jvm.java(List.of("-Xmx512m", "-cp", Jvm.classes(), PKG + "HeaderFootprint")).output();
    String compact = Jvm.java(List.of("-XX:+UseCompactObjectHeaders", "-Xmx512m", "-cp", Jvm.classes(),
        PKG + "HeaderFootprint")).output();
    assertThat(regular).contains("Bytes per Page object: 24");
    assertThat(compact).contains("UseCompactObjectHeaders=true").contains("Bytes per Page object: 16");
  }

  @Test
  void generationalShenandoahRunsYoungCollections() throws Exception {
    Jvm.Result r = Jvm.java(List.of("-XX:+UseShenandoahGC", "-XX:ShenandoahGCMode=generational",
        "-Xmx256m", "-Xlog:gc", "-cp", Jvm.classes(), PKG + "AllocationLoad"));
    assertThat(r.exitCode()).isZero();
    assertThat(r.output()).contains("Using Shenandoah").contains("(Young)").contains("Allocated 409 MB");
  }

  @Test
  void aotCacheIsCreatedInOneStepAndUsed() throws Exception {
    Path jar = jarOfClasses();
    Path cache = tmp.resolve("app.aot");
    Jvm.Result training = Jvm.java(List.of("-XX:AOTCacheOutput=" + cache, "-cp", jar.toString(), PKG + "StartupApp"));
    assertThat(training.output()).contains("AOTCache creation is complete");
    assertThat(cache).exists();

    Jvm.Result run = Jvm.java(List.of("-XX:AOTCache=" + cache, "-Xlog:aot", "-cp", jar.toString(), PKG + "StartupApp"));
    assertThat(run.exitCode()).isZero();
    assertThat(run.output()).contains("Valid ISBNs: 2").doesNotContain("error");
  }

  @Test
  void jfrMethodTimingCountsInvocations() throws Exception {
    Path jfr = tmp.resolve("timing.jfr");
    Jvm.java(List.of("-XX:StartFlightRecording:jdk.MethodTiming#filter=" + PKG + "CatalogSearch::search,filename=" + jfr,
        "-cp", Jvm.classes(), PKG + "CatalogSearch"));
    Jvm.Result view = Jvm.run(Jvm.tool("jfr"), List.of("view", "method-timing", jfr.toString()), null);
    assertThat(view.output()).contains(PKG + "CatalogSearch.search(String)").contains("2,000");
  }

  @Test
  @EnabledOnOs(OS.LINUX)
  void jfrCpuTimeSamplingOnLinux() throws Exception {
    Path jfr = tmp.resolve("cpu.jfr");
    Jvm.java(List.of("-XX:StartFlightRecording=jdk.CPUTimeSample#enabled=true,filename=" + jfr,
        "-cp", Jvm.classes(), PKG + "CatalogSearch"));
    Jvm.Result view = Jvm.run(Jvm.tool("jfr"), List.of("view", "cpu-time-hot-methods", jfr.toString()), null);
    assertThat(view.output()).contains("CPU Time Sampler").contains("CatalogSearch.search(String)");
  }

  private Path jarOfClasses() throws IOException {
    Path root = Path.of(Jvm.classes());
    Path jar = tmp.resolve("app.jar");
    try (OutputStream out = Files.newOutputStream(jar); JarOutputStream jos = new JarOutputStream(out);
        Stream<Path> files = Files.walk(root)) {
      for (Path file : files.filter(Files::isRegularFile).toList()) {
        jos.putNextEntry(new JarEntry(root.relativize(file).toString().replace('\\', '/')));
        Files.copy(file, jos);
        jos.closeEntry();
      }
    }
    return jar;
  }
}
