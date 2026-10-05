package com.howtodoinjava.tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Runs the programs in the scripts folder the same way a reader runs them
 * from a terminal, and checks what they print.
 */
class FirstProgramsTest {

    private static final Path SCRIPTS = Path.of("scripts");
    private static final Path JAVA_BIN = Path.of(System.getProperty("java.home"), "bin");

    @Test
    void compactSourceFileRunsWithoutCompiling() throws Exception {
        String output = run(null, tool("java"), SCRIPTS.resolve("Hello.java").toString());
        assertEquals("Hello, Java!", output);
    }

    @Test
    void compactSourceFileReadsInput() throws Exception {
        String output = run("Lokesh\n", tool("java"), SCRIPTS.resolve("Greeting.java").toString());
        assertEquals("Your name: Hello, Lokesh! Start with basics.", output);
    }

    @Test
    void compactSourceFileHandlesEmptyInput() throws Exception {
        String output = run("", tool("java"), SCRIPTS.resolve("Greeting.java").toString());
        assertEquals("Your name: Hello, stranger! Start with basics.", output);
    }

    @Test
    void classicClassCompilesAndRuns(@TempDir Path out) throws Exception {
        run(null, tool("javac"), "-d", out.toString(), SCRIPTS.resolve("HelloWorld.java").toString());
        assertEquals(true, Files.exists(out.resolve("HelloWorld.class")));

        String output = run(null, tool("java"), "-cp", out.toString(), "HelloWorld");
        assertEquals("Hello, Java!", output);
    }

    private static String tool(String name) {
        return JAVA_BIN.resolve(name).toString();
    }

    private static String run(String stdin, String... command) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>(List.of(command));
        ProcessBuilder builder = new ProcessBuilder(cmd).redirectErrorStream(true);
        builder.environment().remove("JAVA_TOOL_OPTIONS");
        Process process = builder.start();
        try (var in = process.getOutputStream()) {
            if (stdin != null) {
                in.write(stdin.getBytes(StandardCharsets.UTF_8));
            }
        }
        if (!process.waitFor(60, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            throw new IllegalStateException("Timed out: " + cmd);
        }
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8).strip();
        assertEquals(0, process.exitValue(), output);
        return output;
    }
}
