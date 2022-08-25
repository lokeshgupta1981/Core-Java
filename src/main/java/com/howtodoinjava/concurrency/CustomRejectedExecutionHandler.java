package com.howtodoinjava.concurrency;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try
        {
            Thread.sleep(1000);
            System.out.println("Try again : " + ((DemoTask) r).getName());
            executor.execute(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
