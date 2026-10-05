package com.howtodoinjava.moduleimport;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaBaseExportsTest {

  // Packages that "import module java.base" imports: the unqualified exports of java.base
  private static Set<String> exportedPackages(String module) {
    return ModuleFinder.ofSystem().find(module).orElseThrow().descriptor().exports().stream()
        .filter(e -> !e.isQualified())
        .map(ModuleDescriptor.Exports::source)
        .collect(Collectors.toCollection(TreeSet::new));
  }

  @Test
  void javaBaseExports58PackagesInJdk25() {
    Set<String> packages = exportedPackages("java.base");
    System.out.println("java.base exports " + packages.size() + " packages: " + packages);

    assertThat(packages)
        .hasSize(58)
        .contains("java.io", "java.math", "java.net", "java.nio.file", "java.time",
            "java.util", "java.util.concurrent", "java.util.function", "java.util.stream")
        .doesNotContain("java.sql", "java.awt", "java.util.logging");
  }

  @Test
  void javaSeExportsNothingButRequiresJavaBaseTransitively() {
    ModuleDescriptor javaSe = ModuleFinder.ofSystem().find("java.se").orElseThrow().descriptor();

    assertThat(javaSe.exports()).isEmpty();
    assertThat(javaSe.requires())
        .anyMatch(r -> r.name().equals("java.base")
            && r.modifiers().contains(ModuleDescriptor.Requires.Modifier.TRANSITIVE));
  }
}
