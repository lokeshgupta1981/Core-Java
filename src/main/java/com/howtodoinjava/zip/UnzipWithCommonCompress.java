package com.howtodoinjava.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UnzipWithCommonCompress {

  public static void main(String[] args) throws IOException, ArchiveException {
    Path zipFile = Path.of("c:/temp/data.zip");
    extractZip(zipFile);
  }

  private static void extractZip(Path zipFilePath) {
    Path parentDir = zipFilePath.getParent();
    String fileName = zipFilePath.toFile().getName();
    Path targetDir = parentDir.resolve(FilenameUtils.removeExtension(fileName));

    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

    try (InputStream inputStream = Files.newInputStream(zipFilePath);
         ArchiveInputStream archiveInputStream = archiveStreamFactory
             .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream)) {

      ArchiveEntry archiveEntry = null;
      while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
        Path path = Paths.get(targetDir.toString(), archiveEntry.getName());
        File file = path.toFile();
        if (archiveEntry.isDirectory()) {
          if (!file.isDirectory()) {
            file.mkdirs();
          }
        } else {
          File parent = file.getParentFile();
          if (!parent.isDirectory()) {
            parent.mkdirs();
          }
          try (OutputStream outputStream = Files.newOutputStream(path)) {
            IOUtils.copy(archiveInputStream, outputStream);
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ArchiveException e) {
      throw new RuntimeException(e);
    }
  }
}
