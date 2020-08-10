package com.howtodoinjava.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileCopyExample {

	private static void fileCopyUsingApacheCommons() throws IOException 
	{
	    File fileToCopy = new File("c:/temp/testoriginal.txt");
	    File newFile = new File("c:/temp/testcopied.txt");
	 
	    FileUtils.copyFile(fileToCopy, newFile);
	 
	    // OR
	 
	    IOUtils.copy(new FileInputStream(fileToCopy), new FileOutputStream(newFile));
	}
	
	private static void fileCopyUsingNIOFilesClass() throws IOException 
	{
		Path source = Paths.get("c:/temp/testoriginal.txt");
		Path destination = Paths.get("c:/temp/testcopied.txt");

		Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
	}
	
	private static void fileCopyUsingNIOChannelClass() throws IOException 
	{
		File fileToCopy = new File("c:/temp/testoriginal.txt");
		FileInputStream inputStream = new FileInputStream(fileToCopy);
		FileChannel inChannel = inputStream.getChannel();

		File newFile = new File("c:/temp/testcopied.txt");
		FileOutputStream outputStream = new FileOutputStream(newFile);
		FileChannel outChannel = outputStream.getChannel();

		inChannel.transferTo(0, fileToCopy.length(), outChannel);

		inputStream.close();
		outputStream.close();
	}
	
	void fileCopyUsingGuava() throws IOException 
	{
		File fileToCopy = new File("c:/temp/testoriginal.txt");
		File newFile = new File("c:/temp/testcopied.txt");

		com.google.common.io.Files.copy(fileToCopy, newFile);
	}
}

