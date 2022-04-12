package com.howtodoinjava.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ReadFileWithChannels {
  public static void main(final String[] args) {
    // 1. Small files
    try (RandomAccessFile aFile = new RandomAccessFile("test.txt", "r");
      FileChannel inChannel = aFile.getChannel();) {

      long fileSize = inChannel.size();

      // Create buffer of the file size
      ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
      inChannel.read(buffer);
      buffer.flip();

      // Verify the file content
      for (int i = 0; i < fileSize; i++) {
        System.out.print((char) buffer.get());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 2. Large file in chunks
    try (RandomAccessFile aFile = new RandomAccessFile("test.txt", "r");
      FileChannel inChannel = aFile.getChannel();) {

      // Buffer size is 1024
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      while (inChannel.read(buffer) > 0) {
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
          System.out.print((char) buffer.get());
        }
        buffer.clear(); // do something with the data and clear/compact it.
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 3. Memory mapped files
    try (RandomAccessFile aFile = new RandomAccessFile("test.txt", "r");
      FileChannel inChannel = aFile.getChannel();) {

      MappedByteBuffer buffer = inChannel
        .map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());

      buffer.load();
      for (int i = 0; i < buffer.limit(); i++) {
        System.out.print((char) buffer.get());
      }
      buffer.clear(); // do something with the data and clear/compact it.

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
