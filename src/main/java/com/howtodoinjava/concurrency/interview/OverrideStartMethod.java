package com.howtodoinjava.concurrency.interview;

public class OverrideStartMethod {

  public static void main(String[] args) {
    ChildThread childThread = new ChildThread();
    childThread.start();
  }

}

class ChildThread extends Thread {

  public void start() {
    super.start();
    System.out.println("Overriding start() method in ChildThread");
  }

  public void run(){
    System.out.println("ChildThread run() method");
  }
}
