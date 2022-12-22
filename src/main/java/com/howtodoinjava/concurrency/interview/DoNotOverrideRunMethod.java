package com.howtodoinjava.concurrency.interview;

public class DoNotOverrideRunMethod {

  public static void main(String[] args) {
    SubTask subtask = new SubTask();
    subtask.start();
  }
}

class SubTask extends Thread { }
