package com.howtodoinjava.concurrency;

import java.util.concurrent.*;

public class BlockingThreadPoolExecutor extends ThreadPoolExecutor {

  private final Semaphore semaphore;

  public BlockingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit, BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    semaphore = new Semaphore(maximumPoolSize);
  }

  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    super.beforeExecute(t, r);
  }


  @Override
  public void execute(final Runnable task) {

    boolean acquired = false;

    do {
      try {
        semaphore.acquire();
        acquired = true;
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }
    } while (!acquired);

    try {
      super.execute(task);
    } catch (final RejectedExecutionException e) {
      System.out.println("Task Rejected");
      semaphore.release();
      return;
    }
    semaphore.release();
  }

  @Override
  protected void afterExecute(Runnable r, Throwable t) {
    super.afterExecute(r, t);
    if (t != null) {
      t.printStackTrace();
    }
  }
}