package com.howtodoinjava.java26;

import static org.assertj.core.api.Assertions.assertThat;

import com.howtodoinjava.java26.runtime.StartupApp;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AotCacheWithZgcTest {

  static final String MAIN = "com.howtodoinjava.java26.runtime.StartupApp";

  @Test
  void zgcUsesTheAotCache(@TempDir Path dir) throws Exception {
    // the AOT cache accepts only JAR files on the class path
    Path jar = dir.resolve("app.jar");
    String entry = MAIN.replace('.', '/') + ".class";
    try (OutputStream file = Files.newOutputStream(jar);
         JarOutputStream out = new JarOutputStream(file);
         InputStream in = StartupApp.class.getResourceAsStream("StartupApp.class")) {
      out.putNextEntry(new JarEntry(entry));
      in.transferTo(out);
      out.closeEntry();
    }
    Path cache = dir.resolve("app.aot");
    String training = ChildJvm.run(List.of("-XX:+UseZGC", "-XX:AOTCacheOutput=" + cache), jar.toString(), MAIN);
    assertThat(Files.exists(cache)).as(training).isTrue();

    String production = ChildJvm.run(List.of("-XX:+UseZGC", "-XX:AOTCache=" + cache, "-Xlog:aot"), jar.toString(), MAIN);
    production.lines().filter(l -> l.contains("full module graph") || l.contains("AOT-linked") || l.contains("apple")).forEach(System.out::println);
    assertThat(production).contains("full module graph: enabled").contains("Using AOT-linked classes: true").contains("{apple=5, banana=6, cherry=6}");
  }
}
