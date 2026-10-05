package com.howtodoinjava.app;

import com.howtodoinjava.greeter.Greeter;
import com.howtodoinjava.report.ReportHeader;
import com.howtodoinjava.welcome.WelcomeService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.security.CodeSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The greeter-bom import aligns greeter to 2.0.0 for every path in the tree.
 */
class AlignedVersionTest {

  @Test
  void greeterComesFromVersion200() {
    CodeSource source = Greeter.class.getProtectionDomain().getCodeSource();
    String location = (source != null) ? source.getLocation().toString() : "JDK class";
    assertTrue(location.contains("greeter-2.0.0") || location.contains("greeter-v2"), location);
  }

  @Test
  void bothLibrariesWork() {
    assertEquals("Hello, Lokesh - weekly report", ReportHeader.header("Lokesh"));
    assertEquals("Hola, Lokesh", WelcomeService.welcome("Lokesh", "es"));
  }

  @Test
  void reflectionFallsBackWhenMethodIsMissing() {
    String text;
    try {
      Method greet = Greeter.class.getMethod("greet", String.class, String.class);
      text = (String) greet.invoke(null, "Lokesh", "es");
    } catch (NoSuchMethodException e) {
      text = Greeter.greet("Lokesh");
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException("Cannot call greet()", e);
    }
    assertEquals("Hola, Lokesh", text);
  }
}
