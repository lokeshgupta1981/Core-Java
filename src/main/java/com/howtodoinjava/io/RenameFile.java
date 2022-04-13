package com.howtodoinjava.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

public class RenameFile {
  @SuppressWarnings("unused")
  public static void main(final String[] args) throws IOException 
  {
    File originalFile = new File("c:/temp/demo.txt");
    File renamedFile = new File("c:/temp/demoNew.txt");
    File movedFile = new File("c:/temp/moved/demoNew.txt");
    
    //1 - Rename
    boolean isCopied = originalFile.renameTo(renamedFile);
    boolean isMoved = renamedFile.renameTo(movedFile);

    System.out.println(isCopied);
    System.out.println(isMoved);
        
    //2 - Move
    Path file = Path.of("c:/temp/demo.txt");

    //Rename in same directory
    Files.move(file, file.resolveSibling("demoNew.txt"));

    //Move to new directory
    Path newDir = Path.of("c:/temp/moved/");
    Files.move(file, newDir.resolve(file.getFileName()),
        StandardCopyOption.ATOMIC_MOVE, 
        StandardCopyOption.REPLACE_EXISTING);
    
    //3 - Guava
    com.google.common.io.Files.move(originalFile, renamedFile);
    com.google.common.io.Files.move(renamedFile, movedFile);
    
    //4 - Commons IO
    FileUtils.moveFile(originalFile, renamedFile);
    
    File targetDirectory = new File("c:/temp/newLocation/");
    FileUtils.moveFileToDirectory(originalFile, targetDirectory, true);
  }
}
