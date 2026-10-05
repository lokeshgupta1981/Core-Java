package com.howtodoinjava.classversion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

/**
 * Bytecode tools (Gradle's Groovy, Spring, JaCoCo, Byte Buddy, Lombok) read class files with ASM
 * or a copy of it. An ASM version older than the class file throws
 * "Unsupported class file major version 69".
 */
class OldAsmTest {

  private static final Path CLASSES = Path.of(System.getProperty("classes.dir", "target/classes"));
  private static final Path OLD_ASM = Path.of(System.getProperty("old.asm.jar", "target/old-asm/asm-old.jar"));

  @Test
  void oldAsmRejectsJava25ClassFile() throws Exception {
    byte[] bytes = Files.readAllBytes(CLASSES.resolve("com/howtodoinjava/classversion/Hello.class"));

    // ASM 9.7.1 in its own class loader, so it does not clash with ASM 9.10.1 on the test classpath
    try (URLClassLoader loader = new URLClassLoader(new URL[] {OLD_ASM.toUri().toURL()},
        ClassLoader.getPlatformClassLoader())) {
      Class<?> oldReader = loader.loadClass("org.objectweb.asm.ClassReader");
      Constructor<?> constructor = oldReader.getConstructor(byte[].class);

      Throwable thrown = catchThrowable(() -> constructor.newInstance((Object) bytes));

      assertThat(thrown).isInstanceOf(InvocationTargetException.class);
      assertThat(thrown.getCause())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Unsupported class file major version 69");
    }
  }

  @Test
  void currentAsmReadsJava25ClassFile() throws Exception {
    byte[] bytes = Files.readAllBytes(CLASSES.resolve("com/howtodoinjava/classversion/Hello.class"));

    ClassReader reader = new ClassReader(bytes);

    assertThat(reader.getClassName()).isEqualTo("com/howtodoinjava/classversion/Hello");
  }
}
