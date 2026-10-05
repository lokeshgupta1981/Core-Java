package com.howtodoinjava.stackoverflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.howtodoinjava.stackoverflow.tree.Folder;
import com.howtodoinjava.stackoverflow.tree.FolderCounter;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class FolderCounterTest {

  private final Folder deep = Folder.chain(100_000);

  @Test
  void recursionAndDequeAgreeOnSmallTree() {
    Folder music = new Folder("music")
        .add(new Folder("rock").add(new Folder("live")))
        .add(new Folder("jazz"));
    assertEquals(4, FolderCounter.countRecursive(music));
    assertEquals(4, FolderCounter.countWithDeque(music));
  }

  @Test
  void deepTreeOverflowsWithRecursion() {
    assertThrows(StackOverflowError.class, () -> FolderCounter.countRecursive(deep));
  }

  @Test
  void deepTreeWorksWithDeque() {
    assertEquals(100_000, FolderCounter.countWithDeque(deep));
  }

  @Test
  void deepTreeWorksOnThreadWithLargerStack() throws InterruptedException {
    AtomicInteger count = new AtomicInteger();
    Thread worker = Thread.ofPlatform()
        .name("folder-counter")
        .stackSize(16L * 1024 * 1024)
        .unstarted(() -> count.set(FolderCounter.countRecursive(deep)));
    worker.start();
    worker.join();
    assertEquals(100_000, count.get());
  }
}
