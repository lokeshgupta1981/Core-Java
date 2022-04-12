package com.howtodoinjava.io;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class WriteToFile {
  public static void main(final String[] args) throws IOException {
    // 1
    Path filePath = Path.of("demo.txt");
    String content = "hello world !!";

    Files.writeString(filePath, content);

    Files.write(filePath, content.getBytes());

    List<String> lines = Arrays.asList("a", "b", "c");
    Files.write(filePath, lines, StandardCharsets.UTF_8);

    // 2
    try (
      RandomAccessFile stream = new RandomAccessFile(filePath.toFile(), "rw");
      FileChannel channel = stream.getChannel();) {

      byte[] strBytes = content.getBytes();
      ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);

      buffer.put(strBytes);
      buffer.flip();
      channel.write(buffer);
    }

    // 3
    try (BufferedWriter writer = new BufferedWriter(
      new FileWriter(filePath.toFile()))) {
      writer.write(content);
    }

    // 4
    try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
      fileWriter.write(content);
    }

    try (FileWriter fileWriter = new FileWriter(filePath.toFile());
      PrintWriter printWriter = new PrintWriter(fileWriter);) {
      printWriter.print(content);
      printWriter.printf("Blog name is %s", "howtodoinjava.com");
    }

    // 5
    try (
      FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
      byte[] strToBytes = content.getBytes();
      outputStream.write(strToBytes);
    }

    // 6
    try (
      FileOutputStream outputStream = new FileOutputStream(filePath.toFile());
      DataOutputStream dataOutStream = new DataOutputStream(
        new BufferedOutputStream(outputStream));) {
      dataOutStream.writeUTF(content);
      dataOutStream.writeInt(10);
      dataOutStream.writeLong(100L);
    }

  }
}
