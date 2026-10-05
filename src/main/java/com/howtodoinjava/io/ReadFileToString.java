package com.howtodoinjava.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

/**
 * Reads a text file into a String with Files.readString(), Files.readAllBytes(),
 * Files.readAllLines(), Files.lines(), BufferedReader, Scanner, FileInputStream,
 * the classpath, Commons IO and Guava, and shows the charset and line separator
 * pitfalls and how to stream a large file instead of loading it.
 *
 * <p>Run with: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.io.ReadFileToString
 */
public class ReadFileToString {

  public static void main(final String[] args) throws IOException {

    // The sample file used by every example
    Path recipe = Files.createTempFile("recipe-", ".txt");
    Files.writeString(recipe, "Pancakes\nMix flour, milk and one egg.\nFry both sides.\n");

    // 1. Files.readString() - Java 11
    String content = Files.readString(recipe);
    System.out.println("1. readString: " + show(content));
    String latin = Files.readString(recipe, StandardCharsets.ISO_8859_1);
    System.out.println("1. readString with charset: " + show(latin));
    try {
      String missing = Files.readString(Path.of("missing.txt"));
      System.out.println(missing);
    } catch (NoSuchFileException e) {
      System.out.println("1. NoSuchFileException: " + e.getMessage());
    }
    String safe = readOrEmpty(recipe);
    System.out.println("1. readOrEmpty: " + safe.length() + " chars");

    // 2. Files.readAllBytes() - Java 7
    byte[] bytes = Files.readAllBytes(recipe);
    String fromBytes = new String(bytes, StandardCharsets.UTF_8);
    System.out.println("2. readAllBytes: " + bytes.length + " bytes, same text = "
        + fromBytes.equals(content));

    // 3. Files.readAllLines() and Files.lines() joined with a separator
    List<String> lines = Files.readAllLines(recipe);
    String joined = String.join("\n", lines);
    System.out.println("3. readAllLines: " + lines.size() + " lines, " + show(joined));
    String streamed;
    try (Stream<String> stream = Files.lines(recipe)) {
      streamed = stream.collect(Collectors.joining("\n"));
    }
    System.out.println("3. lines: " + show(streamed));
    System.out.println("3. same as readString = " + streamed.equals(content));

    // 4. BufferedReader
    StringBuilder builder = new StringBuilder();
    try (BufferedReader reader = Files.newBufferedReader(recipe, StandardCharsets.UTF_8)) {
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line).append('\n');
      }
    }
    String buffered = builder.toString();
    System.out.println("4. BufferedReader: " + show(buffered));
    String viaLines;
    try (BufferedReader reader = Files.newBufferedReader(recipe)) {
      viaLines = reader.lines().collect(Collectors.joining("\n"));
    }
    System.out.println("4. BufferedReader.lines: " + show(viaLines));

    // 5. Scanner
    String scanned;
    try (Scanner scanner = new Scanner(recipe, StandardCharsets.UTF_8)) {
      scanned = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
    }
    System.out.println("5. Scanner: " + show(scanned));

    // 6. FileInputStream
    String fromStream;
    try (InputStream in = new FileInputStream(recipe.toFile())) {
      fromStream = new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
    System.out.println("6. FileInputStream: " + show(fromStream));

    // 7. A file on the classpath (src/main/resources/recipe.txt)
    String resource = readResource("recipe.txt");
    System.out.println("7. classpath: " + show(resource));

    // 8. Charset pitfall
    Path latinFile = Files.createTempFile("latin-", ".txt");
    Files.writeString(latinFile, "Cr\u00e8me br\u00fbl\u00e9e", StandardCharsets.ISO_8859_1);
    try {
      String wrong = Files.readString(latinFile);
      System.out.println(wrong);
    } catch (MalformedInputException e) {
      System.out.println("8. MalformedInputException: " + e.getMessage());
    }
    String right = Files.readString(latinFile, StandardCharsets.ISO_8859_1);
    System.out.println("8. ISO-8859-1 read: " + right.length() + " chars, correct = "
        + right.equals("Cr\u00e8me br\u00fbl\u00e9e"));

    // 9. Line separator pitfall
    Path windows = Files.createTempFile("windows-", ".txt");
    Files.writeString(windows, "Pancakes\r\nFry both sides.\r\n");
    String keepsCrLf = Files.readString(windows);
    System.out.println("9. readString keeps \\r\\n = " + keepsCrLf.contains("\r\n"));
    String normalized = String.join("\n", Files.readAllLines(windows));
    System.out.println("9. readAllLines joined: " + show(normalized));
    String[] parts = keepsCrLf.split("\\R");
    System.out.println("9. split on \\R: " + parts.length + " parts");

    // 10. Large file: stream the lines instead of loading them
    Path big = Files.createTempFile("big-", ".txt");
    try (Stream<String> stream = Stream.generate(() -> "Fry both sides.").limit(200_000)) {
      Files.write(big, (Iterable<String>) stream::iterator);
    }
    long count;
    try (Stream<String> stream = Files.lines(big)) {
      count = stream.filter(l -> l.contains("Fry")).count();
    }
    System.out.println("10. big file: " + Files.size(big) + " bytes, matching lines = " + count);

    // 11. Commons IO and Guava
    String commons = FileUtils.readFileToString(recipe.toFile(), StandardCharsets.UTF_8);
    System.out.println("11. Commons IO: " + show(commons));
    String guava = com.google.common.io.Files.asCharSource(recipe.toFile(),
        StandardCharsets.UTF_8).read();
    System.out.println("11. Guava: " + show(guava));

    Files.deleteIfExists(recipe);
    Files.deleteIfExists(latinFile);
    Files.deleteIfExists(windows);
    Files.deleteIfExists(big);
  }

  /** Returns the file content, or an empty string when the file cannot be read. */
  static String readOrEmpty(Path path) {
    try {
      return Files.readString(path);
    } catch (IOException e) {
      System.err.println("Cannot read " + path + ": " + e);
      return "";
    }
  }

  /** Reads a text file from the classpath, e.g. src/main/resources/recipe.txt. */
  static String readResource(String name) {
    try (InputStream in = ReadFileToString.class.getClassLoader().getResourceAsStream(name)) {
      if (in == null) {
        throw new IllegalArgumentException(name + " is not on the classpath");
      }
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** Prints line breaks as visible text so the output stays on one line. */
  static String show(String text) {
    return "\"" + text.replace("\r", "\\r").replace("\n", "\\n") + "\"";
  }
}
