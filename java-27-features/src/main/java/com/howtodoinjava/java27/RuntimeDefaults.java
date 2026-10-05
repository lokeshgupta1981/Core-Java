package com.howtodoinjava.java27;

import com.sun.management.HotSpotDiagnosticMXBean;
import java.lang.management.ManagementFactory;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;

/**
 * Reads three JVM defaults that changed in JDK 27 (JEP 523, JEP 534, JEP 527).
 */
public class RuntimeDefaults {

  public static String garbageCollector() {
    return ManagementFactory.getGarbageCollectorMXBeans().getFirst().getName();
  }

  public static boolean compactObjectHeaders() {
    var diag = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
    return Boolean.parseBoolean(diag.getVMOption("UseCompactObjectHeaders").getValue());
  }

  public static List<String> tlsNamedGroups() throws NoSuchAlgorithmException {
    String[] groups = SSLContext.getDefault().getDefaultSSLParameters().getNamedGroups();
    return Arrays.asList(groups);
  }

  public static void main(String[] args) throws Exception {
    System.out.println("GC: " + garbageCollector());
    System.out.println("Compact object headers: " + compactObjectHeaders());
    System.out.println("TLS named groups: " + tlsNamedGroups());
  }
}
