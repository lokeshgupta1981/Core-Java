package com.howtodoinjava.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Creates temporary files and directories with {@link Files#createTempFile} and the legacy
 * {@link File#createTempFile}, prints their paths and POSIX permissions, writes and reads a
 * temp file, and deletes everything with deleteIfExists, DELETE_ON_CLOSE and deleteOnExit.
 *
 * <p>Run with: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.io.CreateTempFile
 */
public class CreateTempFile {

  public static void main(final String[] args) throws IOException {

    // 1. Default temp directory
    System.out.println("java.io.tmpdir = " + System.getProperty("java.io.tmpdir"));

    // 2. NIO Files.createTempFile() in the default temp directory
    Path report = Files.createTempFile("report-", ".csv");
    System.out.println("NIO file: " + report);
    System.out.println("NIO permissions: " + permissions(report));

    // 3. Legacy File.createTempFile()
    File legacy = File.createTempFile("report-", ".csv");
    System.out.println("Legacy file: " + legacy.getAbsolutePath());
    System.out.println("Legacy permissions: " + permissions(legacy.toPath()));

    // 4. Prefix and suffix rules
    Path noSuffix = Files.createTempFile("report-", null);
    System.out.println("Null suffix: " + noSuffix.getFileName());
    Files.delete(noSuffix);
    try {
      File.createTempFile("ab", ".csv");
    } catch (IllegalArgumentException e) {
      System.out.println("Legacy short prefix: " + e.getMessage());
    }
    try {
      Files.createTempFile("reports/2026-", ".csv");
    } catch (IllegalArgumentException e) {
      System.out.println("Separator in prefix: " + e.getMessage());
    }

    // 5. Temp directory and a temp file inside a custom directory
    Path workDir = Files.createTempDirectory("reports-");
    System.out.println("Temp directory: " + workDir);
    System.out.println("Directory permissions: " + permissions(workDir));
    Path daily = Files.createTempFile(workDir, "daily-", ".csv");
    System.out.println("File in custom dir: " + daily);

    // 6. Explicit permissions (the umask still applies)
    Path shared = Files.createTempFile(workDir, "shared-", ".csv",
        PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-")));
    System.out.println("Requested rw-rw-rw-, got: " + permissions(shared));

    // 7. Write and read a temp file, then delete it in finally
    Path data = Files.createTempFile("report-", ".csv");
    try {
      Files.writeString(data, "name,age\nLokesh,37\nAlex,29\n");
      List<String> lines = Files.readAllLines(data);
      System.out.println("Lines read: " + lines);
      System.out.println("Size: " + Files.size(data) + " bytes");
    } finally {
      Files.deleteIfExists(data);
    }
    System.out.println("Exists after finally: " + Files.exists(data));

    // 8. Write with a BufferedWriter
    Path buffered = Files.createTempFile("report-", ".csv");
    try (BufferedWriter writer = Files.newBufferedWriter(buffered)) {
      writer.write("name,age");
      writer.newLine();
      writer.write("Lokesh,37");
    }
    System.out.println("Buffered content: " + Files.readString(buffered).replace("\n", "|"));
    Files.deleteIfExists(buffered);

    // 9. DELETE_ON_CLOSE: write, read back through the same channel, file gone after close
    Path scratch = Files.createTempFile("scratch-", ".bin");
    try (SeekableByteChannel channel = Files.newByteChannel(scratch,
        StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DELETE_ON_CLOSE)) {
      channel.write(ByteBuffer.wrap("Lokesh,37".getBytes(StandardCharsets.UTF_8)));
      channel.position(0);
      ByteBuffer buffer = ByteBuffer.allocate(64);
      channel.read(buffer);
      System.out.println("Read before close: "
          + new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8));
      System.out.println("Exists before close: " + Files.exists(scratch));
    }
    System.out.println("Exists after close: " + Files.exists(scratch));

    // 10. deleteOnExit() does not delete a non-empty directory; delete the tree ourselves
    deleteRecursively(workDir);
    System.out.println("Temp directory exists: " + Files.exists(workDir));

    // 11. Cleanup of the files from steps 2 to 4
    Files.deleteIfExists(report);
    legacy.deleteOnExit();
    System.out.println("Legacy file scheduled for deletion on exit");
  }

  static String permissions(Path path) throws IOException {
    return PosixFilePermissions.toString(Files.getPosixFilePermissions(path));
  }

  /** Deletes a directory and its content, children first. */
  static void deleteRecursively(Path dir) throws IOException {
    if (Files.notExists(dir)) {
      return;
    }
    try (Stream<Path> paths = Files.walk(dir)) {
      paths.sorted(Comparator.reverseOrder()).forEach(path -> {
        try {
          Files.delete(path);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      });
    }
  }
}
