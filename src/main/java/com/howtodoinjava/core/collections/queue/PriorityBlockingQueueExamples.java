package com.howtodoinjava.core.collections.queue;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Java PriorityBlockingQueue: Thread-Safe Priority Queue Examples".
 * https://howtodoinjava.com/java/collections/java-priorityblockingqueue/
 */
public class PriorityBlockingQueueExamples {
    static record PrintJob(String name, int priority) {}
    static record SequencedJob(PrintJob job, long sequence) {}
    public static void main(String[] args) throws Exception {
        {
            PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
            queue.put(42);                                  // never blocks, the queue is unbounded
            queue.put(8);
            queue.put(17);
            Integer head = queue.peek();                    // 8
            show("head", head);
            Integer first = queue.take();                   // 8 (waits only when the queue is empty)
            show("first", first);
            Integer second = queue.poll(1, TimeUnit.SECONDS);   // 17
            show("second", second);
            int spaceLeft = queue.remainingCapacity();      // 2147483647 (Integer.MAX_VALUE)
            show("spaceLeft", spaceLeft);
        }
        {
            PriorityBlockingQueue<String> names = new PriorityBlockingQueue<>(List.of("pear", "apple", "fig"));
            String firstName = names.take();                // "apple"
            show("firstName", firstName);
            String secondName = names.take();               // "fig"
            show("secondName", secondName);
        }
        {
            Comparator<PrintJob> urgentFirst = Comparator.comparingInt(PrintJob::priority).reversed();
            PriorityBlockingQueue<PrintJob> jobs = new PriorityBlockingQueue<>(11, urgentFirst);
            jobs.put(new PrintJob("flyer", 1));
            jobs.put(new PrintJob("invoice", 9));
            jobs.put(new PrintJob("report", 5));
            String next = jobs.take().name();               // "invoice"
            show("next", next);
            String after = jobs.take().name();              // "report"
            show("after", after);
        }
        {
            PriorityBlockingQueue<Integer> pending = new PriorityBlockingQueue<>(List.of(1, 3, 2, 6, 4, 5));
            List<Integer> batch = new ArrayList<>();
            int moved = pending.drainTo(batch, 3);          // 3
            show("moved", moved);
            List<Integer> firstBatch = List.copyOf(batch);  // [1, 2, 3]
            show("firstBatch", firstBatch);
            int movedRest = pending.drainTo(batch);         // 3
            show("movedRest", movedRest);
            List<Integer> all = batch;                      // [1, 2, 3, 4, 5, 6]
            show("all", all);
        }
        {
            Comparator<PrintJob> urgentFirst = Comparator.comparingInt(PrintJob::priority).reversed();
            PriorityBlockingQueue<PrintJob> inbox = new PriorityBlockingQueue<>(11, urgentFirst);
            List<String> printed = new CopyOnWriteArrayList<>();

            Thread printer = Thread.ofVirtual().start(() -> {
                try {
                    PrintJob job = inbox.take();            // waits here until a job arrives
                    printed.add(job.name());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();     // keep the interrupt flag
                }
            });
            inbox.put(new PrintJob("invoice", 9));
            printer.join();
            List<String> done = List.copyOf(printed);       // [invoice]
            show("done", done);
        }
        {
            Comparator<PrintJob> urgentFirst = Comparator.comparingInt(PrintJob::priority).reversed();
            PrintJob stop = new PrintJob("STOP", Integer.MIN_VALUE);
            PriorityBlockingQueue<PrintJob> spool = new PriorityBlockingQueue<>(11, urgentFirst);
            spool.put(new PrintJob("flyer", 1));
            spool.put(new PrintJob("contract", 9));
            spool.put(new PrintJob("report", 5));
            spool.put(new PrintJob("payslip", 7));
            spool.put(stop);

            List<String> printOrder = new CopyOnWriteArrayList<>();
            Thread worker = Thread.ofVirtual().start(() -> {
                try {
                    PrintJob job;
                    while ((job = spool.take()) != stop) {
                        printOrder.add(job.name());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            worker.join();
            List<String> printedJobs = List.copyOf(printOrder);   // [contract, payslip, report, flyer]
            show("printedJobs", printedJobs);
        }
        {
            AtomicLong sequence = new AtomicLong();
            PriorityBlockingQueue<SequencedJob> fair = new PriorityBlockingQueue<>(11,
            Comparator.comparingInt((SequencedJob s) -> s.job().priority()).reversed()
                    .thenComparingLong(SequencedJob::sequence));
            fair.put(new SequencedJob(new PrintJob("memo-a", 3), sequence.getAndIncrement()));
            fair.put(new SequencedJob(new PrintJob("memo-b", 3), sequence.getAndIncrement()));
            String firstMemo = fair.take().job().name();    // "memo-a"
            show("firstMemo", firstMemo);
        }
        {
            PriorityBlockingQueue<Integer> idle = new PriorityBlockingQueue<>();
            Integer none = idle.poll();                         // null
            show("none", none);
            Integer timedOut = idle.poll(10, TimeUnit.MILLISECONDS);   // null after 10 ms
            show("timedOut", timedOut);
        }
        {
            PriorityBlockingQueue<Integer> heap = new PriorityBlockingQueue<>(List.of(42, 17, 99, 8, 23));
            List<Integer> sortedCopy = heap.stream().sorted().toList();   // [8, 17, 23, 42, 99]
            show("sortedCopy", sortedCopy);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
