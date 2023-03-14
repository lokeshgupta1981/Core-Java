package com.howtodoinjava.hashing.password.demo.md5;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileChecksum {

  public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

    Path filePath = Path.of("c:/temp/test.txt");

    //Using MD5
    byte[] data = Files.readAllBytes(filePath);
    byte[] hash = MessageDigest.getInstance("MD5").digest(data);
    String checksum = new BigInteger(1, hash).toString(16);

    System.out.println(checksum);

    //Using SHA-256
    data = Files.readAllBytes(filePath);
    hash = MessageDigest.getInstance("SHA-256").digest(data);
    checksum = new BigInteger(1, hash).toString(16);

    System.out.println(checksum);

    //Guava
    File file = new File("c:/temp/test.txt");
    ByteSource byteSource = com.google.common.io.Files.asByteSource(file);
    HashCode hc = byteSource.hash(Hashing.md5());
    checksum = hc.toString();

    System.out.println(checksum);

    byteSource = com.google.common.io.Files.asByteSource(file);
    hc = byteSource.hash(Hashing.sha256());
    checksum = hc.toString();

    System.out.println(checksum);
  }

}
