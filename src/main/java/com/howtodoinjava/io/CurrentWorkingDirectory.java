package com.howtodoinjava.io;

import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Get Current Working Directory in Java (user.dir and Path)".
 * https://howtodoinjava.com/java/io/get-current-working-directory/
 */
public class CurrentWorkingDirectory {
    static class ReportJob {

        static Path codeLocation() throws URISyntaxException {
            URL location = ReportJob.class.getProtectionDomain().getCodeSource().getLocation();
            return Path.of(location.toURI());
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String userDir = System.getProperty("user.dir");                     // /home/lokesh/demo
            show("userDir", userDir);
            Path cwd = Path.of("").toAbsolutePath();                             // /home/lokesh/demo
            show("cwd", cwd);
            Path cwdFs = FileSystems.getDefault().getPath("").toAbsolutePath();  // /home/lokesh/demo
            show("cwdFs", cwdFs);
            String fileEmpty = new File("").getAbsolutePath();                   // /home/lokesh/demo
            show("fileEmpty", fileEmpty);

            Path dot = Path.of(".").toAbsolutePath();                            // /home/lokesh/demo/.
            show("dot", dot);
            Path dotNormalized = Path.of(".").toAbsolutePath().normalize();      // /home/lokesh/demo
            show("dotNormalized", dotNormalized);
        }
        {
            String currentWorkingDir = System.getProperty("user.dir");   // /home/lokesh/demo
            show("currentWorkingDir", currentWorkingDir);
        }
        {
            Path cwd = Path.of("").toAbsolutePath();                              // /home/lokesh/demo
            show("cwd", cwd);
            Path configFile = cwd.resolve("config").resolve("app.properties");   // /home/lokesh/demo/config/app.properties
            show("configFile", configFile);
            boolean exists = Files.exists(configFile);                           // false
            show("exists", exists);
        }
        {
            Path real = Path.of("").toRealPath();                      // /home/lokesh/demo
            show("real", real);
            String canonical = new File(".").getCanonicalPath();       // /home/lokesh/demo
            show("canonical", canonical);
        }
        {
            String fileEmpty = new File("").getAbsolutePath();       // /home/lokesh/demo
            show("fileEmpty", fileEmpty);
            Path asPath = new File("").toPath().toAbsolutePath();     // /home/lokesh/demo
            show("asPath", asPath);
        }
        {
            Path location = ReportJob.codeLocation();      // /opt/reports/report-job.jar (target/classes in the IDE)
            show("location", location);
            Path appFolder = location.getParent();          // /opt/reports
            show("appFolder", appFolder);
        }
        {
            String old = System.setProperty("user.dir", "/tmp");
            String changed = System.getProperty("user.dir");          // "/tmp"
            show("changed", changed);
            String stillOld = new File("x").getAbsolutePath();        // /home/lokesh/demo/x
            show("stillOld", stillOld);
            Path stillOldPath = Path.of("").toAbsolutePath();         // /home/lokesh/demo
            show("stillOldPath", stillOldPath);
            String restored = System.setProperty("user.dir", old);    // "/tmp"
            show("restored", restored);
        }
        {
            Process process = new ProcessBuilder("pwd").directory(new File("/tmp")).start();
            String childDir;
            try (InputStream out = process.getInputStream()) {
                childDir = new String(out.readAllBytes()).strip();                          // "/tmp"
            }
            int exitCode = process.waitFor();                                               // 0
            show("exitCode", exitCode);
        }
        {
            long entries;
            try (Stream<Path> files = Files.list(Path.of(""))) {
                entries = files.count();
            }
            boolean hasEntries = entries > 0;        // true
            show("hasEntries", hasEntries);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
