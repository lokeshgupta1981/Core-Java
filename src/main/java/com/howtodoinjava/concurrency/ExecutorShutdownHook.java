package com.howtodoinjava.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Why a forgotten ExecutorService keeps the JVM alive, and how a shutdown hook stops the pool
 * when the process receives Ctrl+C or SIGTERM.
 *
 * <p>Run without arguments to see the JVM keep running after main() returns (stop it with Ctrl+C):
 * mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.ExecutorShutdownHook
 *
 * <p>Run with the argument "hook" and stop the process with Ctrl+C (or kill -TERM) to see the
 * shutdown hook wait for the running task:
 * java -cp target/classes com.howtodoinjava.concurrency.ExecutorShutdownHook hook
 */
public class ExecutorShutdownHook {

  public static void main(String[] args) {
    ExecutorService pool = Executors.newFixedThreadPool(2);

    if (args.length > 0 && args[0].equals("hook")) {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("shutdown hook started");
        boolean terminated = ExecutorShutdownExamples.shutdownAndAwaitTermination(pool, 5, TimeUnit.SECONDS);
        System.out.println("pool terminated = " + terminated);
      }));
      pool.submit(ExecutorShutdownExamples.sendEmail("email-1", 2000));
    } else {
      pool.submit(ExecutorShutdownExamples.sendEmail("email-1", 200));
    }
    System.out.println("main() finished");
  }
}
