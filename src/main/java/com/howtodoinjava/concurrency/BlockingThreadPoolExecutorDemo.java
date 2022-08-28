package com.howtodoinjava.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingThreadPoolExecutorDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1);
        BlockingThreadPoolExecutor executor = new BlockingThreadPoolExecutor(1, 1, 5000, TimeUnit.MILLISECONDS, blockingQueue);
        executor.setRejectedExecutionHandler(new CustomRejectedExecutionHandler());

        executor.prestartAllCoreThreads();

        int threadCounter = 0;
        while (true) {
            threadCounter++;
            // Adding threads one by one
            System.out.println("Adding DemoTask : " + threadCounter);
            blockingQueue.offer(new DemoTask(Integer.toString(threadCounter)));
            if (threadCounter == 100)
                break;
        }

        Thread.sleep(1000000);
    }
}
