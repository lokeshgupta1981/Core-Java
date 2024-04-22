package com.howtodoinjava.core.security;

import jakarta.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class Base64FileEncodeExample {

  public static void main(String[] args) throws IOException {
    //TextFile
    File file = new File("c:/temp/output.txt");
    String encoded = Base64.getEncoder()
        .encodeToString(Files.readAllBytes(file.toPath()));
    System.out.println(encoded);

    byte[] decoded = Base64.getDecoder().decode(encoded);
    System.out.println(new String(decoded));

    //ImageFile
    File inImage = new File("c:/temp/avni.jpg");
    File outImage = new File("c:/temp/avni_new.jpg");
    byte[] encodedImageBytes = Base64.getEncoder().encode(Files.readAllBytes(inImage.toPath()));
    byte[] decodedImageBytes = Base64.getDecoder().decode(encodedImageBytes);
    Files.write(outImage.toPath(), decodedImageBytes);
  }

}
