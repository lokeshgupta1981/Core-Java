package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  static Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] asrg){

    ArrayList<String> tokens = new ArrayList<>();
    tokens.add("active");

    Record record = new Record(1, "value", tokens);
    System.out.println(record);   //Record{id=1, name='value', tokens=[active]}

    record.tokens().add("new token");
    System.out.println(record);   ////Record{id=1, name='value', tokens=[active]}
  }
}

record Record(long id, String name, List<String> tokens){
  public List<String> tokens() {
    return new ArrayList<>(tokens);
  }
}
