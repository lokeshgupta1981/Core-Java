package com.howtodoinjava.concurrency;

public class IllegalMonitorStateExceptionDemo
{

  public static void main(String[] args) {
    WaitingThread waitingThread = new WaitingThread();
    NotifyingThread notifyingThread  = new NotifyingThread();

    waitingThread.start();
    notifyingThread.start();
  }

  //This object is used for synchronization among threads.
  public final static Object obj = new Object();

  public static class WaitingThread extends Thread {

    @Override
    public void run() {
      try {
        synchronized(obj) {
          //Calling wait() method outside of synchronized area
          obj.wait();    // Raises IllegalMonitorStateException
        }
      }
      catch (InterruptedException ex) {
        System.out.println(Thread.currentThread().getName() + " gets Interrupted");
      }
    }
  }

  public static class NotifyingThread extends Thread {

    @Override
    public void run() {
      try {
        // Thread sleep for 5 sec
        Thread.sleep(5000);
        synchronized(obj) {
          // Calling notify() outside of synchronized area
          obj.notify();         // Raises IllegalMonitorStateException
        }
      }
      catch (InterruptedException ex) {
        System.err.println(Thread.currentThread().getName() + " gets Interrupted");
      }
    }
  }
}
