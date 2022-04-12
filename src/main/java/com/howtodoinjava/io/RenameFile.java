package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

public class RenameFile {
  @SuppressWarnings("unused")
  public static void main(final String[] args) throws IOException 
  {
    File originalFile = new File("c:/temp/demo.txt");
    File renamedFile = new File("c:/temp/demoNew.txt");
    
    //1 - Rename
    boolean isCopied = originalFile.renameTo(renamedFile);
        
    //2 - Move
    Files.move(originalFile.toPath(), renamedFile.toPath());
    Files.move(originalFile.toPath(), renamedFile.toPath(), 
        StandardCopyOption.ATOMIC_MOVE, 
        StandardCopyOption.REPLACE_EXISTING);
    
    //3 - Guava
    com.google.common.io.Files.move(originalFile, renamedFile);
    
    //4 - Commons IO
    FileUtils.moveFile(originalFile, renamedFile);
    
    File targetDirectory = new File("c:/temp/newLocation/");
    FileUtils.moveFileToDirectory(originalFile, targetDirectory, true);
  }
}
