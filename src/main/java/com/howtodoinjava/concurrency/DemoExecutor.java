package com.howtodoinjava.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DemoExecutor {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> blockingQueue =
                new LinkedBlockingQueue<Runnable>();

        CustomThreadPoolExecutor executor =
                new CustomThreadPoolExecutor(10, 20, 5, TimeUnit.SECONDS,
                        blockingQueue, new ThreadPoolExecutor.AbortPolicy());

        // Let start all core threads initially
        executor.prestartAllCoreThreads();

        for (int i = 1; i <= 100; i++) {
            blockingQueue.offer(new DemoTask("Task " + i));
        }

        executor.shutdown();
        executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
}
