package com.howtodoinjava.concurrency;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shutting down an ExecutorService: the standard shutdown pattern, close() with
 * try-with-resources (Java 19+), what happens to queued tasks, RejectedExecutionException,
 * isShutdown() vs isTerminated(), tasks that ignore interrupts, virtual-thread executors
 * and daemon pool threads.
 *
 * <p>Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.ExecutorShutdownExamples
 */
public class ExecutorShutdownExamples {

  public static void main(String[] args) throws Exception {
    standardPattern();
    closeWithTryWithResources();
    queuedTasksAndShutdownNow();
    rejectedAfterShutdown();
    shutdownStates();
    taskIgnoringInterrupts();
    virtualThreadExecutor();
    daemonThreads();
  }

  /** The standard pattern from the ExecutorService Javadoc, returning true when the pool terminated. */
  static boolean shutdownAndAwaitTermination(ExecutorService pool, long timeout, TimeUnit unit) {
    pool.shutdown();                                   // 1. Stop accepting new tasks
    try {
      if (!pool.awaitTermination(timeout, unit)) {     // 2. Wait for running and queued tasks
        pool.shutdownNow();                            // 3. Interrupt the tasks still running
        if (!pool.awaitTermination(timeout, unit)) {   // 4. Wait for them to react
          System.err.println("Pool did not terminate");
          return false;
        }
      }
    } catch (InterruptedException e) {
      pool.shutdownNow();
      Thread.currentThread().interrupt();              // 5. Keep the interrupt flag
    }
    return pool.isTerminated();
  }

  /** A task that "sends" an email and takes the given time. */
  static Runnable sendEmail(String name, long millis) {
    return () -> {
      try {
        TimeUnit.MILLISECONDS.sleep (millis);
        System.out.println(name + " sent");
      } catch (InterruptedException e) {
        System.out.println(name + " interrupted");
        Thread.currentThread().interrupt();
      }
    };
  }

  static void standardPattern() {
    System.out.println("== 1. Standard pattern");
    ExecutorService pool = Executors.newFixedThreadPool(2);
    pool.submit(sendEmail("email-1", 200));
    pool.submit(sendEmail("email-2", 200));

    boolean terminated = shutdownAndAwaitTermination(pool, 5, TimeUnit.SECONDS);
    System.out.println("terminated = " + terminated);
  }

  static void closeWithTryWithResources() {
    System.out.println("== 2. close() with try-with-resources");
    ExecutorService copy;
    try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
      copy = pool;
      pool.submit(sendEmail("email-1", 200));
      pool.submit(sendEmail("email-2", 200));
    } // close() = shutdown() + wait until all tasks finish
    System.out.println("after try block, terminated = " + copy.isTerminated());
  }

  static void queuedTasksAndShutdownNow() throws InterruptedException {
    System.out.println("== 3. Queued tasks: shutdown() vs shutdownNow()");
    ExecutorService pool = Executors.newSingleThreadExecutor();
    pool.submit(sendEmail("email-1", 300));
    Future<?> email2 = pool.submit(sendEmail("email-2", 300));
    pool.submit(sendEmail("email-3", 300));
    TimeUnit.MILLISECONDS.sleep (100);                 // email-1 is running, 2 and 3 wait in the queue

    List<Runnable> neverStarted = pool.shutdownNow();
    System.out.println("never started = " + neverStarted.size());
    System.out.println("terminated in time = " + pool.awaitTermination(1, TimeUnit.SECONDS));
    System.out.println("email-2 done = " + email2.isDone());   // get() would block forever

    for (Runnable task : neverStarted) {
      if (task instanceof Future<?> future) {
        future.cancel(false);
      }
    }
    System.out.println("email-2 cancelled = " + email2.isCancelled());
  }

  static void rejectedAfterShutdown() {
    System.out.println("== 4. Submitting after shutdown()");
    ExecutorService pool = Executors.newFixedThreadPool(2);
    pool.shutdown();
    try {
      pool.submit(sendEmail("email-4", 100));
    } catch (RejectedExecutionException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void shutdownStates() throws InterruptedException {
    System.out.println("== 5. isShutdown() vs isTerminated()");
    ExecutorService pool = Executors.newFixedThreadPool(2);
    pool.submit(sendEmail("email-1", 300));
    System.out.println("running:    isShutdown=" + pool.isShutdown() + " isTerminated=" + pool.isTerminated());
    pool.shutdown();
    System.out.println("shutdown(): isShutdown=" + pool.isShutdown() + " isTerminated=" + pool.isTerminated());
    pool.awaitTermination(5, TimeUnit.SECONDS);
    System.out.println("finished:   isShutdown=" + pool.isShutdown() + " isTerminated=" + pool.isTerminated());
  }

  static void taskIgnoringInterrupts() throws InterruptedException {
    System.out.println("== 6. A task that ignores interrupts");
    ExecutorService pool = Executors.newSingleThreadExecutor();
    long end = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(1500);
    pool.submit(() -> {
      while (System.nanoTime() < end) {
        // busy loop, never checks Thread.currentThread().isInterrupted()
      }
      System.out.println("busy loop finished");
    });
    TimeUnit.MILLISECONDS.sleep (100);
    pool.shutdownNow();
    System.out.println("terminated after 500 ms = " + pool.awaitTermination(500, TimeUnit.MILLISECONDS));
    System.out.println("terminated after 2 s    = " + pool.awaitTermination(2, TimeUnit.SECONDS));
  }

  static void virtualThreadExecutor() {
    System.out.println("== 7. Virtual-thread executor");
    AtomicInteger sent = new AtomicInteger();
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < 10_000; i++) {
        executor.submit(() -> {
          TimeUnit.MILLISECONDS.sleep (100);
          return sent.incrementAndGet();
        });
      }
    } // waits for all 10,000 tasks
    System.out.println("sent = " + sent.get());
  }

  static void daemonThreads() throws Exception {
    System.out.println("== 8. Pool threads are non-daemon by default");
    ExecutorService pool = Executors.newFixedThreadPool(1);
    Future<Boolean> daemon = pool.submit(() -> Thread.currentThread().isDaemon());
    System.out.println("default pool thread daemon = " + daemon.get());
    pool.shutdown();

    ThreadFactory daemonFactory = Thread.ofPlatform().name("mailer-", 1).daemon().factory();
    ExecutorService daemonPool = Executors.newFixedThreadPool(2, daemonFactory);
    Future<String> name = daemonPool.submit(() -> Thread.currentThread().getName() + " daemon=" + Thread.currentThread().isDaemon());
    System.out.println(name.get());
    daemonPool.shutdown();
  }
}
