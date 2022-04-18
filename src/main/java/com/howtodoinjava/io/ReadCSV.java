package com.howtodoinjava.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class ReadCSV {
  public static void main(String[] args) {
    // 1. OpenCSV
    try(CSVReader reader
            = new CSVReader(new FileReader("SampleCSVFile.csv")))
    {
      String [] nextLine;

      //Read one line at a time
      while ((nextLine = reader.readNext()) != null)
      {
        //Use the tokens as required
        System.out.println(Arrays.toString(nextLine));
      }
    }
    catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }

    //2 Scanner
    //Get scanner instance
    try(Scanner scanner = new Scanner(new File("SampleCSVFile.csv"))){

      //Read line
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        //Scan the line for tokens
        try (Scanner rowScanner = new Scanner(line)) {
          rowScanner.useDelimiter(",");
          while (rowScanner.hasNext()) {
            System.out.print(scanner.next());
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    //3 Splitting


    try(BufferedReader fileReader
            = new BufferedReader(new FileReader("SampleCSVFile.csv")))
    {
      String line = "";

      //Read the file line by line
      while ((line = fileReader.readLine()) != null)
      {
        //Get all tokens available in line
        String[] tokens = line.split(",");

        //Verify tokens
        System.out.println(Arrays.toString(tokens));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }


  }

}
