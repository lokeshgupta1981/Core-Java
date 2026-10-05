package com.howtodoinjava.stackoverflow;

import com.howtodoinjava.stackoverflow.tree.Folder;
import com.howtodoinjava.stackoverflow.tree.FolderCounter;
import tools.jackson.databind.json.JsonMapper;

/** Prints the outputs shown in the article. */
public final class Runner {

  private Runner() {
  }

  public static void main(String[] args) {
    System.out.println("sumTo(5) = " + Sums.sumTo(5));
    System.out.println("sumToLoop(1_000_000) = " + Sums.sumToLoop(1_000_000));

    // 1. Trimmed stack trace of the missing base case
    try {
      Sums.sumToNoBase(5);
    } catch (StackOverflowError e) {
      StackTraceElement[] frames = e.getStackTrace();
      System.out.println(e + " with " + frames.length + " frames in getStackTrace()");
      for (int i = 0; i < 3; i++) {
        System.out.println("\tat " + frames[i]);
      }
    }

    // 2. Deep but correct recursion vs explicit Deque
    Folder deep = Folder.chain(100_000);
    System.out.println("countWithDeque = " + FolderCounter.countWithDeque(deep));
    try {
      FolderCounter.countRecursive(deep);
    } catch (StackOverflowError e) {
      System.out.println("countRecursive -> " + e);
    }

    // 3. toString() cycle
    var loop = new com.howtodoinjava.stackoverflow.cycle.broken.Playlist("Road Trip");
    loop.addSong(new com.howtodoinjava.stackoverflow.cycle.broken.Song("Song A"));
    try {
      System.out.println(loop);
    } catch (StackOverflowError e) {
      StackTraceElement[] frames = e.getStackTrace();
      System.out.println(e);
      for (int i = 0; i < 12; i++) {
        System.out.println("\tat " + frames[i]);
      }
    }
    var playlist = new com.howtodoinjava.stackoverflow.cycle.fixed.Playlist("Road Trip");
    playlist.addSong(new com.howtodoinjava.stackoverflow.cycle.fixed.Song("Song A"));
    System.out.println(playlist);

    // 4. Jackson
    JsonMapper mapper = JsonMapper.builder().build();
    var broken = new com.howtodoinjava.stackoverflow.cycle.broken.Playlist("Road Trip");
    broken.addSong(new com.howtodoinjava.stackoverflow.cycle.broken.Song("Song A"));
    try {
      mapper.writeValueAsString(broken);
    } catch (Exception e) {
      String message = e.getMessage();
      System.out.println(e.getClass().getName() + ": " + message.substring(0, Math.min(260, message.length())) + " ...");
    }
    var fixed = new com.howtodoinjava.stackoverflow.jackson.fixed.Playlist("Road Trip");
    fixed.addSong(new com.howtodoinjava.stackoverflow.jackson.fixed.Song("Song A"));
    String json = mapper.writeValueAsString(fixed);
    System.out.println(json);
    var back = mapper.readValue(json, com.howtodoinjava.stackoverflow.jackson.fixed.Playlist.class);
    System.out.println("back-reference restored: " + (back.getSongs().getFirst().getPlaylist() == back));
    var ignored = new com.howtodoinjava.stackoverflow.jackson.ignore.Playlist("Road Trip");
    ignored.addSong(new com.howtodoinjava.stackoverflow.jackson.ignore.Song("Song A"));
    System.out.println(mapper.writeValueAsString(ignored));
  }
}
