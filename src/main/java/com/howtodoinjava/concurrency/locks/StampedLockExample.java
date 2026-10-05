package com.howtodoinjava.concurrency.locks;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock optimistic read: read the fields without locking, then validate the stamp. If a write
 * happened in between, validate() returns false and we read again under a real read lock.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.StampedLockExample
 */
public class StampedLockExample {

  static class Score {
    private final StampedLock lock = new StampedLock();
    private int home;
    private int away;

    void goal(boolean forHome) {
      long stamp = lock.writeLock();
      try {
        if (forHome) {
          home++;
        } else {
          away++;
        }
      } finally {
        lock.unlockWrite(stamp);
      }
    }

    String read() {
      long stamp = lock.tryOptimisticRead();      // no locking, returns a stamp
      int h = home;
      int a = away;
      if (!lock.validate(stamp)) {                // a write happened: read again with a read lock
        stamp = lock.readLock();
        try {
          h = home;
          a = away;
        } finally {
          lock.unlockRead(stamp);
        }
      }
      return h + ":" + a;
    }
  }

  public static void main(String[] args) {
    Score score = new Score();
    score.goal(true);
    System.out.println("score = " + score.read());

    // Show validate() in one thread: a write between the optimistic read and validate()
    long stamp = score.lock.tryOptimisticRead();
    System.out.println("validate, no write: " + score.lock.validate(stamp));
    score.goal(false);
    System.out.println("validate, after a write: " + score.lock.validate(stamp));
    System.out.println("score = " + score.read());
  }
}
