# Java Locks

Source code for the article [Java Locks (with Examples)](https://howtodoinjava.com/java/multi-threading/how-to-use-locks-in-java-java-util-concurrent-locks-lock-tutorial-and-example/).

Java 21 or later (tested on Java 25; *VirtualThreadLockExample* also on Java 21). Each class is a standalone program:

| Class | Topic |
|---|---|
| LockCounterExample | Race condition on a counter, fixed with lock() / try / finally unlock() |
| ReentrancyExample | getHoldCount(), unlock() without lock() |
| TryLockExample | tryLock() and tryLock(timeout) |
| LockInterruptiblyExample | lockInterruptibly() and interrupt() |
| FairLockExample | Fair vs unfair ReentrantLock |
| ReadWriteLockExample | ReentrantReadWriteLock with three readers at once |
| StampedLockExample | Optimistic read and validate() |
| ConditionExample | Producer-consumer queue with two Conditions |
| VirtualThreadLockExample | Blocking inside synchronized vs ReentrantLock on virtual threads |

Run one example:

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.LockCounterExample
```

Run the virtual thread comparison with two carrier threads:

```bash
MAVEN_OPTS="-Djdk.virtualThreadScheduler.parallelism=2" mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.concurrency.locks.VirtualThreadLockExample
```
